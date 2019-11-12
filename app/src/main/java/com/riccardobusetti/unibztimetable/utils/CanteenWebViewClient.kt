package com.riccardobusetti.unibztimetable.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity

class CanteenWebViewClient(private val context: Context) : WebViewClient() {

    companion object {
        private const val BASE_URL = "https://unibz.markas.info"
        private const val MENU_URL_PATH = "menu"

        const val CANTEEN_URL = "$BASE_URL/$MENU_URL_PATH"
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url != null && url.contains(CANTEEN_URL)) {
            // This is my web site, so do not override; let my WebView load the page
            return false
        }

        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            startActivity(context, this, null)
        }

        return true
    }
}