package com.example.globalinfocountries.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.globalinfocountries.data.repository.NewsRepositoryInterface
import com.example.globalinfocountries.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelNews @Inject constructor(
    private val repository: NewsRepositoryInterface,
):ViewModel()

{
    private val _uiState = MutableStateFlow(UiStatesNews())
    val uiState :StateFlow<UiStatesNews> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<UiEffectNews>()
    val effect :SharedFlow<UiEffectNews> = _effect.asSharedFlow()

 private fun loadingNew(code:String){
        viewModelScope.launch {
            Log.d("DEBUG_NEWS", "VM News loading code: $code")

            repository.getNewsByCountry(code)
                .onStart {
                   _uiState.value = _uiState.value.copy(isLoadingNews = true)
                    }
                .collect{ result ->
                    when(result){
                        is DataResult.Success ->{
                            _uiState.value = UiStatesNews(isLoadingNews = false,
                                listNews = result.data
                            )
                        }
                        is DataResult.Error ->{
                            _uiState.value = _uiState.value.copy(isLoadingNews = false)
                            _effect.emit(UiEffectNews.ShowErrorNews(result.error))
                        }
                    }

                }
        }
    }
    fun onEvent(event: EventNews){
        when(event){
            is EventNews.LoadNews->{
                Log.d("DEBUG_NEWS", "Loading news for code onEvent: ${event.code}")

                loadingNew(event.code)
            }
            is EventNews.Refresh ->{
                loadingNew(event.code)
            }
            is EventNews.OnClickLink->{
                viewModelScope.launch {
                    _effect.emit(UiEffectNews.OnOpenLink(event.uri))
                }
            }
        }
    }
}