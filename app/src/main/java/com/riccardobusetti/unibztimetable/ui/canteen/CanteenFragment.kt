package com.riccardobusetti.unibztimetable.ui.canteen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.riccardobusetti.unibztimetable.R
import com.riccardobusetti.unibztimetable.utils.CanteenWebViewClient
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

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupUi() {
        webView = fragment_canteen_web_view
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = CanteenWebViewClient(context!!)

        webView.loadUrl(CanteenWebViewClient.CANTEEN_URL)
    }

}