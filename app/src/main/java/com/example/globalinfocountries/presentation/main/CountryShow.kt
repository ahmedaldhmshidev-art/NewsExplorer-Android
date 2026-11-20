package com.example.globalinfocountries.presentation.main

import android.os.Bundle

import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.globalinfocountries.R
import com.example.globalinfocountries.databinding.FragmentCountoryShowBinding
import com.example.globalinfocountries.domain.model.Country
import com.example.globalinfocountries.utils.Exception.UiError
import com.example.globalinfocountries.presentation.countries.AdapterCountry
import com.example.globalinfocountries.presentation.countries.EventsCountry
import com.example.globalinfocountries.presentation.countries.UiEffectCountry
import com.example.globalinfocountries.presentation.countries.ViewModelCountry
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountryShow : Fragment() {

    // ----------------------------
    // ViewBinding
    // ----------------------------
    private lateinit var binding: FragmentCountoryShowBinding

    // ----------------------------
    // Adapter للـ RecyclerView
    // ----------------------------
    private lateinit var adapterCountry: AdapterCountry

    // ----------------------------
    // ViewModel باستخدام Hilt
    // ----------------------------
    private val viewModelCountry: ViewModelCountry by viewModels()

    // ----------------------------
    // onCreateView
    // ----------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // تهيئة الـ ViewBinding
        binding = FragmentCountoryShowBinding.inflate(inflater, container, false)

        // إعداد RecyclerView و Adapter
        setupRecyclerView()

        // طلب تحميل الدول عند فتح الشاشة
        viewModelCountry.onEvent(EventsCountry.Load)

        // متابعة UiState لتحديث الـ UI
        observerUiState()

        // متابعة UiEffect للأحداث لمرة واحدة مثل التنقل أو إظهار الأخطاء
        observerEffect()

        // إعداد SwipeRefresh لإعادة تحميل البيانات عند السحب للأسفل
        setupSwipeRefresh()

        // ربط شريط البحث بالـ ViewModel
        binding.etSearchCountryId.doOnTextChanged { text, _, _, _ ->
            // عند كتابة أي نص، نرسل Event إلى ViewModel لتحديث البحث
            viewModelCountry.onEvent(EventsCountry.OnSearchQueryChanged(text.toString()))
        }

        return binding.root
    }

    // ----------------------------
    // إعداد RecyclerView + Adapter
    // ----------------------------
    private fun setupRecyclerView() {
        adapterCountry = AdapterCountry { code ->
            // عند النقر على دولة، نرسل Event للتنقل إلى الشاشة التالية
            viewModelCountry.onEvent(EventsCountry.OnCountryClicked(code))
        }
        binding.rvShowCountryId.adapter = adapterCountry
        binding.rvShowCountryId.layoutManager = LinearLayoutManager(requireContext())
    }

    // ----------------------------
    // متابعة UiState
    // ----------------------------
    private fun observerUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            // repeatOnLifecycle يحمي التطبيق من leaks ويجمع الـ State فقط عندما يكون Fragment مرئي
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelCountry.uiState.collect { uiState ->
                    // تحديث Loading (SwipeRefresh)
                    showProgressLoading(uiState.isLoading)
                    // تحديث قائمة الدول في الـ Adapter
                    adapterCountry.submit(uiState.listCountry)
                }
            }
        }
    }

    // ----------------------------
    // متابعة UiEffect
    // ----------------------------
    private fun observerEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelCountry.effect.collect { effect ->
                    when (effect) {
                        is UiEffectCountry.Navigation -> {
                            // عند حدث Navigation، ننتقل إلى الشاشة التالية
                            val action = CountryShowDirections.actionCountoryShowToNewsShow(effect.code)
                            findNavController().navigate(action)
                        }
                        is UiEffectCountry.ShowErrorCountry -> {
                            // عند حدث Error، نعرض Snackbar
                            showErrorSnackbar(effect.error)
                        }
                    }
                }
            }
        }
    }

    // ----------------------------
    // عرض Snackbar عند الخطأ
    // ----------------------------
    private fun showErrorSnackbar(error: UiError) {
        val snackbar = Snackbar.make(
            binding.root,
            getString(error.messageRes),
            Snackbar.LENGTH_LONG
        )
        // إذا كان بالإمكان إعادة المحاولة
        if (error.canRetry) {
            snackbar.setAction(getString(R.string.error_retry)) {
                viewModelCountry.onEvent(EventsCountry.Refresh)
            }
        }
        snackbar.show()
    }

    // ----------------------------
    // إعداد SwipeRefresh لإعادة تحميل البيانات
    // ----------------------------
    private fun setupSwipeRefresh() {
        binding.swipeRefreshIdCountry.setOnRefreshListener {
            viewModelCountry.onEvent(EventsCountry.Refresh)
            binding.etSearchCountryId.text = null

        }
    }

    // ----------------------------
    // تحديث حالة الـ Loading (SwipeRefresh)
    // ----------------------------
    private fun showProgressLoading(result: Boolean) {
        binding.swipeRefreshIdCountry.isRefreshing = result
    }
}






//@AndroidEntryPoint
//class CountryShow : Fragment() {
//
//    private lateinit var binding: FragmentCountoryShowBinding
//    private lateinit var adapterCountry: AdapterCountry
//    private  val viewModelCountry : ViewModelCountry by viewModels()
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        binding = FragmentCountoryShowBinding.inflate(inflater,container,false)
//
////        RV+adapter
//        setupRecyclerView()
////        vm loading
//        viewModelCountry.onEvent(EventsCountry.Load)
//        observerUiState()
//
//        observerEffect()
//
//        setupSwipeRefresh()
//
//        adapterCountry.submit(getSampleCountry())
//
//        return binding.root
//
//    }
//
//    private fun getSampleCountry(): List<Country> {
//        return listOf(
//            Country(
//                name = "yemen",
//                capital = "Sanaa",
//                code = "en",
//                flagUrl = "",
//                population = 10000
//            )
//        )
//
//    }
//
//    private fun setupRecyclerView() {
////        ندئ الادابتر
//        adapterCountry = AdapterCountry { code ->
//            Log.d("adapter:", "code adapter CountryShow: = $code")
////            عند النقر علئ دولة ينتقل
//            viewModelCountry.onEvent(EventsCountry.OnCountryClicked(code))
//        }
//        binding.rvShowCountryId.adapter = adapterCountry
//        binding.rvShowCountryId.layoutManager = LinearLayoutManager(requireContext())
//    }
//    //        vm uiState
//    private fun observerUiState() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModelCountry.uiState.collect { uiState ->
//
//                    showProgressLoading(uiState.isLoading)
//                    adapterCountry.submit(uiState.listCountry)
//                }
//            }
//        }
//    }
//
//    private fun observerEffect(){
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModelCountry.effect.collect { effect ->
//                    when (effect) {
//                        is UiEffectCountry.Navigation -> {
//                            Log.d("aaaaaaaaaaaaa", "bbbb = ${effect.code}")
//                            val action =
//                                CountryShowDirections.actionCountoryShowToNewsShow(effect.code)
//                            findNavController().navigate(action)
//                        }
//                        is UiEffectCountry.ShowErrorCountry ->{
//                            showErrorSnackbar(effect.error)
//                        }
//                    }
//                }
//            }
//        }
//        binding.etSearchCountryId.doOnTextChanged { text, _, _, _ ->
//            viewModelCountry.onSearchQueryChanged(text.toString())
//        }
//        lifecycleScope.launch {
//            viewModelCountry.countries.collect{
//                adapterCountry.submit(it)
//            }
//        }
//    }
//
//
//
//
//    private fun showErrorSnackbar(error: UiError) {
//      val snackbar = Snackbar.make(binding.root ,
//          getString(error.messageRes),
//          Snackbar.LENGTH_LONG
//          )
//        if (error.canRetry){
//            snackbar.setAction(getString(R.string.error_retry)){
//                viewModelCountry.onEvent(EventsCountry.Refresh)
//            }
//        }
//        snackbar.show()
//    }
//
//    private fun setupSwipeRefresh() {
//        binding.swipeRefreshIdCountry.setOnRefreshListener {
//            viewModelCountry.onEvent(EventsCountry.Refresh)
//        }
//    }
//
//
//    private fun showProgressLoading(result :Boolean) {
//        binding.swipeRefreshIdCountry.isRefreshing = result
//    }
//
//
//}