package com.riccardobusetti.unibztimetable.ui.canteen

import android.annotation.SuppressLint
import android.content.res.Configuration.*
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.data.remote.WebSiteUrl
import com.riccardobusetti.unibztimetable.utils.custom.StrictWebViewClient
import kotlinx.android.synthetic.main.fragment_canteen.*


class CanteenFragment : Fragment() {

    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_canteen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
    }

    private fun setupUi() {
        webView = fragment_canteen_web_view
        applyWebViewSettings()
        webView.loadUrl(WebSiteUrl.BASE_CANTEEN_URL)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun applyWebViewSettings() {
        webView.settings.javaScriptEnabled = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val nightModeFlags = resources.configuration.uiMode and UI_MODE_NIGHT_MASK

            if (nightModeFlags == UI_MODE_NIGHT_YES) {
                webView.settings.forceDark = WebSettings.FORCE_DARK_ON
            } else if (nightModeFlags == UI_MODE_NIGHT_NO) {
                webView.settings.forceDark = WebSettings.FORCE_DARK_OFF
            }
        }

        webView.webViewClient = StrictWebViewClient(
            context!!,
            listOf(WebSiteUrl.CANTEEN_URL_REGEX)
        )
    }
}