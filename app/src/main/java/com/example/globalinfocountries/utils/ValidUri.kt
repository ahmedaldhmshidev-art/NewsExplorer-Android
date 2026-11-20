package com.example.globalinfocountries.utils

import android.net.Uri
    // دالة للتحقق من صحة الرابط
    fun isValidUri(amgUri: String): Boolean {
        return try {
            if (amgUri.isNullOrEmpty()) return false
//               Uri.parse(url) → يحوّل النص (String) الموجود في url إلى كائن من نوع Uri.
            val uri = Uri.parse(amgUri)
//               uri.scheme → يرجع البروتوكول (scheme) المستخدم في الرابط، مثل http, https, ftp.
//                == "http" → يتحقق إذا كان البروتوكول هو http.
            uri.scheme == "http" || uri.scheme =="https"
        }
        catch (e:Exception){ false }
    }

