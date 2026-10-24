package com.lastwave.app.ui.metro

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.lastwave.app.ui.common.ExpressiveHeader
import com.lastwave.app.ui.common.adaptiveContentWidth

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SquidifyHubScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                setSupportZoom(false)
                loadWithOverviewMode = true
                useWideViewPort = true
                allowFileAccess = false
                allowContentAccess = false
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                offscreenPreRaster = true
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    return false
                }
            }

            webChromeClient = WebChromeClient()

            setDownloadListener { url, userAgent, contentDisposition, mimetype, _ ->
                try {
                    val filename = URLUtil.guessFileName(url, contentDisposition, mimetype)
                    val request = DownloadManager.Request(Uri.parse(url)).apply {
                        setMimeType(mimetype)
                        addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
                        addRequestHeader("User-Agent", userAgent)
                        setDescription("Downloading audio via Squidify...")
                        setTitle(filename)
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_MUSIC,
                            "AnantaDhvani/$filename",
                        )
                    }
                    val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    dm.enqueue(request)
                    Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                }
            }

            loadUrl("https://www.squidify.org/")
        }
    }

    BackHandler {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .adaptiveContentWidth(maxWidth = 860.dp),
        ) {
            ExpressiveHeader(
                title = "Squidify Audio Hub",
                subtitle = "Web streaming & lossless track downloader",
                onBack = { navController.popBackStack() },
            )
            AndroidView(
                factory = { webView },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
