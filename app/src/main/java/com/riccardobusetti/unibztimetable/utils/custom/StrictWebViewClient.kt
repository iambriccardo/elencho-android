package com.riccardobusetti.unibztimetable.utils.custom

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity
import com.riccardobusetti.unibztimetable.utils.StringUtils

/**
 * Custom [WebViewClient] which will load only permitted urls inside of the webview and
 * launch the others in the device browser.
 *
 * @author Riccardo Busetti
 */
class StrictWebViewClient(
    private val context: Context,
    private val permittedRegexes: List<String>
) : WebViewClient() {

    private var startCallback: (() -> Unit)? = null
    private var finishCallback: (() -> Unit)? = null

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (url != null) {
            permittedRegexes.forEach {
                // This is my web site, so do not override; let my WebView load the page
                if (StringUtils.isMatchingPartially(it, url)) return false
            }
        }

        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            startActivity(context, this, null)
        }

        return true
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        startCallback?.let { it() }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        finishCallback?.let { it() }
    }

    fun listenForProgress(startCallback: () -> Unit, finishCallback: () -> Unit) {
        this.startCallback = startCallback
        this.finishCallback = finishCallback
    }
}