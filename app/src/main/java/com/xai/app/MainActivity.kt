package com.xai.app

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xai.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var webView: WebView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val targetUrl = "https://www.xaiswp.app"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        setupWebView()
        
        // Show loading animation for 2 seconds, then load website
        Handler(Looper.getMainLooper()).postDelayed({
            loadWebsite()
        }, 2000)
    }

    private fun initializeViews() {
        webView = binding.webView
        swipeRefreshLayout = binding.swipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {
            webView.reload()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            cacheMode = WebSettings.LOAD_DEFAULT
            userAgentString = "XAI-Native-App/1.0 (Android)"
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            useWideViewPort = true
            loadWithOverviewMode = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                swipeRefreshLayout.isRefreshing = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeRefreshLayout.isRefreshing = false
                hideLoadingAnimation()
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                hideLoadingAnimation()
                showErrorDialog(description ?: "Unknown error occurred")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    hideLoadingAnimation()
                }
            }
        }
    }

    private fun loadWebsite() {
        webView.loadUrl(targetUrl)
    }

    private fun hideLoadingAnimation() {
        binding.loadingLayout.visibility = View.GONE
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.error))
            .setMessage("Failed to load website: $message")
            .setPositiveButton(getString(R.string.retry)) { _, _ ->
                binding.loadingLayout.visibility = View.VISIBLE
                webView.reload()
            }
            .setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}