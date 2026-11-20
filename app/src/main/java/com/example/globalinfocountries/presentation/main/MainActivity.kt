package com.example.globalinfocountries.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.globalinfocountries.R
import com.example.globalinfocountries.data.local.AppDatabase
import com.example.globalinfocountries.data.local.dao.NewsDao
import com.example.globalinfocountries.data.remote.response.RetrofitNews
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var newsDao: NewsDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolBarId)
        setSupportActionBar(toolbar)

        val fragmentNavHost =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentIdActivity) as NavHost
        navController = fragmentNavHost.navController

        setupActionBarWithNavController(navController)


            lifecycleScope.launch {
                try {
                    val response = RetrofitNews.api.getNewsByCountry("us")
                    Log.d("rrr", "MainActivity List Size: ${response.news.size}")

//                    هنا joinToString بيحوّل القائمة كلها إلى نص واحد مفصول بفواصل، وبيطبع كل عنصر باستخدام toString()
                    Log.d("rrr", "News List: ${response.news.joinToString { it.toString() }}")

                } catch (e: Exception) {
                    Log.e("NEWS_TEST", "خطأ: ${e.message}", e)
                }
            }

        val db = AppDatabase.getDatabase(this)
        newsDao = db.newsDao()


    lifecycleScope.launch {
        try {
            // استدعاء الدالة من Room
            val localNewsList = newsDao.getNewsByCodeFromRoom("ru")

            // حجم القائمة
            Log.d("rrr", "Room List Size: ${localNewsList.size}")

            // طباعة كل العناصر مفصولة بفواصل
            Log.d("rrr", "Room News List: ${localNewsList.joinToString { it.toString() }}")

        } catch (e: Exception) {
            Log.e("NEWS_TEST", "خطأ في قراءة البيانات من Room: ${e.message}", e)
        }
    }

}
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}

