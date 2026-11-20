package com.example.globalinfocountries.data.remote.response

import com.example.globalinfocountries.data.remote.api.ApiNews
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitNews {
//    الشكل المختصر للكاين
//    private val logging = HttpLoggingInterceptor.Level.BODY
    private val logging = HttpLoggingInterceptor().apply{
    level = HttpLoggingInterceptor.Level.BODY
//        هوا HttpLoggingInterceptor()  كلاس جاهز وظيفتة : مراقبة كل طلب واستجابة للل Http
//        وعن طريق ال apply تم اعادة صياغة الكائن ليعطينا مستوا الجسم كامل
//    Level لتحديد كمية التفاصيل الذي سينتم تسجيلها
    }


    private val client = OkHttpClient.Builder()
//    . المحرك OkHttpClient الاساسي لشبكة الذي تعتمد علي في retrofit
        .addInterceptor(logging)
//        اضافة المراقب ليمر علية كل الطلبات والاستجابات
        .build()

    //    link the news
    private const val BASE_URL = "https://api.currentsapi.services/"

   val retrofit by lazy {
       Retrofit.Builder()
           .baseUrl(BASE_URL)
           .client(client)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
   }
    val api : ApiNews by lazy {
        retrofit.create(ApiNews::class.java)
    }
}