package com.lastwave.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.widget.RemoteViews
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.lastwave.app.MainActivity
import com.lastwave.app.R
import com.lastwave.app.playback.MusicPlaybackService
import com.lastwave.app.playback.MusicPlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicStableWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        for (appWidgetId in appWidgetIds) {
            updateWidgetUi(context, appWidgetManager, appWidgetId, lastPlayerState)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_UPDATE_WIDGET) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, MusicStableWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            for (appWidgetId in appWidgetIds) {
                updateWidgetUi(context, appWidgetManager, appWidgetId, lastPlayerState)
            }
        }
    }

    companion object {
        const val ACTION_UPDATE_WIDGET = "com.lastwave.app.widget.UPDATE_STABLE_WIDGET"

        @Volatile
        private var lastPlayerState: MusicPlayerState? = null

        fun updateWidget(context: Context, state: MusicPlayerState) {
            lastPlayerState = state
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, MusicStableWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            if (appWidgetIds.isEmpty()) return

            for (appWidgetId in appWidgetIds) {
                updateWidgetUi(context, appWidgetManager, appWidgetId, state)
            }
        }

        private fun updateWidgetUi(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            state: MusicPlayerState?,
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_archivetune_stable)

            val openAppIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val openAppPendingIntent = PendingIntent.getActivity(
                context, 0, openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_stable_container, openAppPendingIntent)

            views.setOnClickPendingIntent(
                R.id.widget_stable_btn_prev,
                getServicePendingIntent(context, MusicPlaybackService.ACTION_PREVIOUS, 401)
            )
            views.setOnClickPendingIntent(
                R.id.widget_stable_btn_play_pause,
                getServicePendingIntent(context, MusicPlaybackService.ACTION_TOGGLE, 402)
            )
            views.setOnClickPendingIntent(
                R.id.widget_stable_btn_next,
                getServicePendingIntent(context, MusicPlaybackService.ACTION_NEXT, 403)
            )

            val track = state?.current
            if (track != null) {
                views.setTextViewText(R.id.widget_stable_title, track.title)
                views.setTextViewText(R.id.widget_stable_artist, track.artist)
                val playPauseIcon = if (state.isPlaying) R.drawable.ic_widget_pause else R.drawable.ic_widget_play
                views.setImageViewResource(R.id.widget_stable_btn_play_pause, playPauseIcon)

                val artworkUrl = track.artworkUrl
                if (!artworkUrl.isNullOrBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        runCatching {
                            val request = ImageRequest.Builder(context)
                                .data(artworkUrl)
                                .size(256)
                                .allowHardware(false)
                                .build()
                            val result = context.imageLoader.execute(request)
                            ((result as? SuccessResult)?.drawable as? BitmapDrawable)?.bitmap
                        }.getOrNull()?.let { bitmap ->
                            views.setImageViewBitmap(R.id.widget_stable_artwork, bitmap)
                            appWidgetManager.updateAppWidget(appWidgetId, views)
                        }
                    }
                } else {
                    views.setImageViewResource(R.id.widget_stable_artwork, R.drawable.ic_launcher_logo)
                }
            } else {
                views.setTextViewText(R.id.widget_stable_title, "LastWaveX Music")
                views.setTextViewText(R.id.widget_stable_artist, "Select a track to play")
                views.setImageViewResource(R.id.widget_stable_btn_play_pause, R.drawable.ic_widget_play)
                views.setImageViewResource(R.id.widget_stable_artwork, R.drawable.ic_launcher_logo)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getServicePendingIntent(context: Context, action: String, requestCode: Int): PendingIntent {
            val intent = Intent(context, MusicPlaybackService::class.java).apply {
                this.action = action
            }
            return PendingIntent.getService(
                context, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
