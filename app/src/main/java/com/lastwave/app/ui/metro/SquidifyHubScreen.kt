package com.lastwave.app.ui.metro

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.Keep
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.lastwave.app.playback.PlayableTrack
import com.lastwave.app.ui.common.ExpressiveHeader
import com.lastwave.app.ui.common.adaptiveContentWidth
import com.lastwave.app.ui.player.LocalMusicPlayer

@Keep
class SquidifyBridge(private val onPlayTrack: (PlayableTrack) -> Unit) {
    @Keep
    @JavascriptInterface
    fun playAudioStream(title: String, artist: String, streamUrl: String, artworkUrl: String) {
        onPlayTrack(
            PlayableTrack(
                title = title,
                artist = artist,
                playbackUrl = streamUrl,
                artworkUrl = artworkUrl,
            ),
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SquidifyHubScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val musicPlayer = LocalMusicPlayer.current
    var isLoading by remember { mutableStateOf(true) }

    val bridge = remember(musicPlayer) {
        SquidifyBridge { track ->
            musicPlayer.play(track)
        }
    }

    val webView = remember {
        WebView(context).apply {
            setBackgroundColor(android.graphics.Color.TRANSPARENT)
            setLayerType(android.view.View.LAYER_TYPE_HARDWARE, null)
            addJavascriptInterface(bridge, "SquidifyBridge")
            addJavascriptInterface(bridge, "AndroidBridge")

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
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    isLoading = true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    isLoading = false
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    return false
                }

                override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                    val url = request?.url?.toString() ?: return super.shouldInterceptRequest(view, request)
                    val lower = url.lowercase()
                    if (lower.contains(".mp3") || lower.contains(".flac") || lower.contains(".m4a") || lower.contains(".aac") || lower.contains("/stream")) {
                        val webTitle = view?.title?.replace("Squidify", "", ignoreCase = true)?.trim(' ', '-', '|', '—').orEmpty()
                        val guessedName = URLUtil.guessFileName(url, null, null).substringBeforeLast(".")
                        val cleanTitle = when {
                            webTitle.isNotBlank() && !webTitle.equals("Home", ignoreCase = true) -> webTitle
                            guessedName.isNotBlank() && !guessedName.contains("stream", ignoreCase = true) -> guessedName
                            else -> "Squidify Audio Stream"
                        }

                        android.os.Handler(android.os.Looper.getMainLooper()).post {
                            view?.evaluateJavascript("document.querySelectorAll('audio, video').forEach(el => { el.pause(); el.currentTime = 0; });", null)
                            musicPlayer.play(
                                PlayableTrack(
                                    title = cleanTitle,
                                    artist = "Squidify Audio Hub",
                                    playbackUrl = url,
                                ),
                            )
                        }
                    }
                    return super.shouldInterceptRequest(view, request)
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                AndroidView(
                    factory = { webView },
                    modifier = Modifier.fillMaxSize(),
                )
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}
