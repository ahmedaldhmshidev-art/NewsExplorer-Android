package com.example.globalinfocountries.presentation.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.globalinfocountries.data.repository.CountryRepositoryInterface
import com.example.globalinfocountries.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
//@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)

// ----------------------------
// ViewModelCountry مع البحث الاحترافي
// ----------------------------
@HiltViewModel
@OptIn(
    kotlinx.coroutines.ExperimentalCoroutinesApi::class, // لدوال مثل flatMapLatest
    kotlinx.coroutines.FlowPreview::class                // لدوال مثل debounce
)
class ViewModelCountry @Inject constructor(
    private val repository: CountryRepositoryInterface
) : ViewModel() {

    // ----------------------------
    // UiState: يمثل حالة الشاشة (Loading + البيانات + نص البحث)
    // ----------------------------
    private val _uiState = MutableStateFlow(UiStatesCountry())
    val uiState: StateFlow<UiStatesCountry> = _uiState.asStateFlow()

    // ----------------------------
    // UiEffect: أحداث لمرة واحدة (Navigation، Error Snackbar)
    // ----------------------------
    private val _effect = MutableSharedFlow<UiEffectCountry>()
    val effect: SharedFlow<UiEffectCountry> = _effect.asSharedFlow()

    // ----------------------------
    // نص البحث الحالي
    // ----------------------------
    private val _searchQuery = MutableStateFlow("")

    // ----------------------------
    // تحديث نص البحث من الـ UI
    // ----------------------------
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    // ----------------------------
    // التعامل مع الأحداث القادمة من الـ UI
    // ----------------------------
    fun onEvent(event: EventsCountry) {
        when (event) {

            is EventsCountry.Load -> {
                // تحميل كل الدول عند فتح الشاشة
                _searchQuery.value = ""
            }

            is EventsCountry.Refresh -> {
                // إعادة تحميل البيانات حسب آخر بحث
                _searchQuery.value = _uiState.value.searchQuery

            }

            is EventsCountry.OnCountryClicked -> {
                // عند النقر على دولة → نرسل UiEffect للتنقل
                viewModelScope.launch {
                    _effect.emit(UiEffectCountry.Navigation(event.countryCode))
                }
            }

            is EventsCountry.OnSearchQueryChanged -> {
                // عند تغيير نص البحث في الـ EditText
                onSearchQueryChanged(event.query)
            }
        }
    }

    // ----------------------------
    // Initialization: نبدأ بمراقبة البحث تلقائيًا
    // ----------------------------
    init {
        observeSearch()
    }

    // ----------------------------
    // مراقبة نص البحث _searchQuery
    // - debounce(300): الانتظار 0.3 ثانية بعد آخر حرف
    // - distinctUntilChanged(): تجاهل النصوص المتكررة
    // - flatMapLatest(): إذا تم تغيير البحث، يلغي البحث السابق
    // ----------------------------
    private fun observeSearch() {
        _searchQuery
            .debounce(300) // الانتظار لتقليل عدد الاستعلامات أثناء الكتابة
            .distinctUntilChanged() // تجاهل نفس النصوص المتكررة
            .flatMapLatest { query ->
                // إذا كان البحث فارغ → نعيد كل الدول
                if (query.isBlank()) {
                    repository.getCountries()
                } else {
                    // البحث حسب النص المدخل
                    repository.searchCountry(query)
                }
            }
            .onStart {
                // عند بدء البحث → نظهر Loading
                _uiState.value = _uiState.value.copy(isLoading = true)
            }
            .onEach { result ->
                // التعامل مع نتيجة البحث أو التحميل
                when (result) {
                    is DataResult.Success -> {
                        // تحديث UiState عند النجاح
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            listCountry = result.data
                        )
                    }
                    is DataResult.Error -> {
                        // عند الخطأ → نوقف Loading ونرسل UiEffect للخطأ
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _effect.emit(UiEffectCountry.ShowErrorCountry(result.error))
                    }
                }
            }
            .launchIn(viewModelScope) // جمع الـ Flow ضمن ViewModel lifecycle
    }
}



//class ViewModelCountry @Inject constructor(private val repository: CountryRepositoryInterface):ViewModel() {
//
//    private val _uiState = MutableStateFlow(UiStatesCountry())
//    val uiState: StateFlow<UiStatesCountry> = _uiState.asStateFlow()
//
//    private val _effect = MutableSharedFlow<UiEffectCountry>()
//    val effect: SharedFlow<UiEffectCountry> = _effect.asSharedFlow()
//
//
//    fun onEvent(event: EventsCountry){
//        when(event){
//            is EventsCountry.Load ->{
//                loadingCountries()
//            }
//
//            is EventsCountry.Refresh ->{
//                loadingCountries()
//            }
//            is EventsCountry.OnSearchQueryChanged->{
//                _uiState.value=_uiState.value.copy(searchQuery = event.query)
//                _searchQuery.value=event.query
//            }
//
//            is EventsCountry.OnCountryClicked ->{
//                Log.d("VIEWMODEL_CLICK", "Navigating code: ${event.countryCode}")
//                viewModelScope.launch {
//                    _effect.emit(UiEffectCountry.Navigation(event.countryCode))
//                }
//            }
//        }
//    }
//
//
// private fun loadingCountries() {
//        viewModelScope.launch {
//            repository.getCountries()
//                .onStart {
//                    _uiState.value = _uiState.value.copy(isLoading = true)
//                }
//                .collect{
//                    result ->
//                    when(result){
//                        is DataResult.Success ->{
//                            _uiState.value = UiStatesCountry(isLoading = false ,
//                                listCountry = result.data)
//                        }
//                        is DataResult.Error ->{
//                            _uiState.value=_uiState.value.copy(isLoading = false)
//                            _effect.emit(UiEffectCountry.ShowErrorCountry(result.error))
//                        }
//                    }
//                }
//        }
//    }
//    private val _searchQuery = MutableStateFlow("")
//
//    fun onSearchQueryChanged(name: String) {
//        _searchQuery.value = name
//    }
//
//    val countries = _searchQuery
//        .debounce(300)
//        .distinctUntilChanged()
//        .flatMapLatest { name ->
//            if (name.isBlank()) {
//                repository.getCountries()
//            } else {
//                repository.searchCountry(name)
//            }
//        }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList<Country>()
//        )
//
//
//}