package com.lastwave.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.widget.RemoteViews
import com.lastwave.app.MainActivity
import com.lastwave.app.R
import com.lastwave.app.playback.MusicPlaybackService

class MusicWidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_PLAY_PAUSE -> sendServiceCommand(context, MusicPlaybackService.ACTION_TOGGLE_PLAYBACK)
            ACTION_NEXT -> sendServiceCommand(context, MusicPlaybackService.ACTION_SKIP_NEXT)
            ACTION_PREVIOUS -> sendServiceCommand(context, MusicPlaybackService.ACTION_SKIP_PREVIOUS)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (widgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId, "Ananta Dhvani", "Tap to play", false, null)
        }
    }

    private fun sendServiceCommand(context: Context, serviceAction: String) {
        val serviceIntent = Intent(context, MusicPlaybackService::class.java).apply {
            action = serviceAction
        }
        try {
            context.startService(serviceIntent)
        } catch (_: Exception) {
            context.startForegroundService(serviceIntent)
        }
    }

    companion object {
        const val ACTION_PLAY_PAUSE = "com.lastwave.app.widget.ACTION_PLAY_PAUSE"
        const val ACTION_NEXT = "com.lastwave.app.widget.ACTION_NEXT"
        const val ACTION_PREVIOUS = "com.lastwave.app.widget.ACTION_PREVIOUS"

        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            widgetId: Int,
            title: String,
            artist: String,
            isPlaying: Boolean,
            artwork: Bitmap?,
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_music_expressive)

            // Open App Intent
            val openAppIntent = Intent(context, MainActivity::class.java)
            val openPendingIntent = PendingIntent.getActivity(
                context, 0, openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            views.setOnClickPendingIntent(R.id.widget_container, openPendingIntent)

            // Action Intents (Broadcast to this receiver)
            views.setOnClickPendingIntent(R.id.widget_btn_play_pause, getActionIntent(context, ACTION_PLAY_PAUSE, widgetId))
            views.setOnClickPendingIntent(R.id.widget_btn_next, getActionIntent(context, ACTION_NEXT, widgetId))
            views.setOnClickPendingIntent(R.id.widget_btn_prev, getActionIntent(context, ACTION_PREVIOUS, widgetId))

            // Metadata & State
            views.setTextViewText(R.id.widget_track_title, title)
            views.setTextViewText(R.id.widget_track_artist, artist)
            views.setImageViewResource(
                R.id.widget_btn_play_pause,
                if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
            )

            // High-Resolution Artwork
            if (artwork != null) {
                views.setImageViewBitmap(R.id.widget_artwork, getRoundedBitmap(artwork, 32f))
            } else {
                views.setImageViewResource(R.id.widget_artwork, R.mipmap.ic_launcher)
            }

            appWidgetManager.updateAppWidget(widgetId, views)
        }

        fun updateAllWidgets(context: Context, title: String, artist: String, isPlaying: Boolean, artwork: Bitmap?) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val ids = appWidgetManager.getAppWidgetIds(ComponentName(context, MusicWidgetProvider::class.java))
            for (id in ids) {
                updateWidget(context, appWidgetManager, id, title, artist, isPlaying, artwork)
            }
        }

        private fun getActionIntent(context: Context, action: String, widgetId: Int): PendingIntent {
            val intent = Intent(context, MusicWidgetProvider::class.java).apply {
                this.action = action
            }
            return PendingIntent.getBroadcast(
                context, widgetId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }

        private fun getRoundedBitmap(bitmap: Bitmap, radius: Float): Bitmap {
            val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val rect = Rect(0, 0, bitmap.width, bitmap.height)
            val rectF = RectF(rect)
            canvas.drawRoundRect(rectF, radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            return output
        }
    }
}
