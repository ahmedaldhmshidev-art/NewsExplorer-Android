package com.example.globalinfocountries.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.globalinfocountries.R
import com.example.globalinfocountries.databinding.FragmentNewsShowBinding
import com.example.globalinfocountries.domain.model.News
import com.example.globalinfocountries.utils.Exception.UiError
import com.example.globalinfocountries.presentation.news.AdapterNews
import com.example.globalinfocountries.presentation.news.EventNews
import com.example.globalinfocountries.presentation.news.UiEffectNews
import com.example.globalinfocountries.presentation.news.ViewModelNews
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class NewsShow : Fragment() {

    private lateinit var binding: FragmentNewsShowBinding
    private val viewModelNews: ViewModelNews by viewModels()
    private lateinit var adapterNews: AdapterNews

    private val args: NewsShowArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DEBUG_NEWS", "NewsShow args code: ${args.codeCountry}")
        viewModelNews.onEvent(EventNews.LoadNews(args.codeCountry))
    }

    private fun getSampleNews(): List<News> {
        return listOf(
            News(
                title = "جاري تحميل الاخبار ..",
                description = "في انتظار الاخبار ",
                imageUrl = "",
                url = "https://www.google.com",
                source = "Ahmed aldhmshi",
                publishedAt = "101000",
                author = "Ahmed"
            )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewsShowBinding.inflate(inflater, container, false)
// call adapter
        setupRecyclerView()
//        مراقبة الحالة
        observerUiState()
//        مراقبة الاحداث
        observerUiEffect()
//        سحب للاسفل لتحديث
        setupSwipeRefresh()

        adapterNews .submitList(getSampleNews())
        return binding.root
    }

    private fun setupRecyclerView() {
        adapterNews = AdapterNews{ uri ->
            viewModelNews.onEvent(EventNews.OnClickLink(uri))
        }
        binding.rvNewsListId.adapter = adapterNews
        binding.rvNewsListId.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observerUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModelNews.uiState.collect{
                        uiState ->
                            Log.d("NEWS_UI", "وصل Success: ${uiState.listNews.size}")
                            Log.d("rrr", "Room News List ViewModel: ${uiState.listNews.joinToString { it.toString() }}")
                            showProgressLoading(uiState.isLoadingNews)
                            adapterNews.submitList(uiState.listNews)
                        }
                    }
                }
            }



    private fun observerUiEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModelNews.effect.collect{ effect ->
                    when(effect){
                        is UiEffectNews.OnOpenLink ->{
                            openLink(effect.uri)
                            Log.d("NEWS_TEST", "خطأ: ${effect.uri}")
                        }
                        is UiEffectNews.ShowErrorNews->{
                            showErrorSnackbar(effect.uiError)
                        }
                    }
                }
            }
        }
    }

    private fun openLink(uri: String) {
        if (uri.isNullOrBlank()) {
            Toast.makeText(requireContext(), getString(R.string.error_linkNoFound), Toast.LENGTH_LONG).show()
            return
        }
        val parsedUri = Uri.parse(uri)
        val intent = Intent(Intent.ACTION_VIEW, parsedUri).apply {
//            CATEGORY_BROWSABLE → يحدد أن الرابط للتصفح.
            addCategory(Intent.CATEGORY_BROWSABLE)
//            FLAG_ACTIVITY_NEW_TASK → يفتح المتصفح في مهمة مستقلة.
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            requireActivity().startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), getString(R.string.error_linkNoOpen), Toast.LENGTH_LONG).show()
            Log.e("NEWS_TEST", "خطأ: ${e.message}", e)

//            printStackTrace() → يطبع تفاصيل الخطأ في الـ Logcat لتعرف السبب.
            e.printStackTrace()
        }
    }


    private fun setupSwipeRefresh() {
        binding.swipeRefreshIdNews.setOnRefreshListener {
            viewModelNews.onEvent(EventNews.Refresh(args.codeCountry))
        }
    }

    private fun showErrorSnackbar(error: UiError) {
        val snackbar = Snackbar.make(binding.root ,
            getString(error.messageRes),
            Snackbar.LENGTH_LONG
        )
        if (error.canRetry){
            snackbar.setAction(getString(R.string.error_retry)){
                viewModelNews.onEvent(EventNews.Refresh(args.codeCountry))
            }
        }
        snackbar.show()
    }

    private fun showProgressLoading(isShow:Boolean) {
        binding.progressIdNewsShow.isVisible=isShow
        binding.swipeRefreshIdNews.isRefreshing=isShow

    }
}
