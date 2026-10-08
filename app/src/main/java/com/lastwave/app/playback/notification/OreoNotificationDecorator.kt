package com.lastwave.app.playback.notification

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.palette.graphics.Palette
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OreoNotificationDecorator @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun decorateNotification(
        builder: NotificationCompat.Builder,
        artworkBitmap: Bitmap?,
        enabled: Boolean,
    ) {
        if (enabled && artworkBitmap != null) {
            runCatching {
                val palette = Palette.from(artworkBitmap).generate()
                val swatch = palette.vibrantSwatch ?: palette.dominantSwatch ?: palette.mutedSwatch
                if (swatch != null) {
                    val saturatedColor = shiftSaturation(swatch.rgb, 0.20f)
                    builder.setColor(saturatedColor)
                    builder.setColorized(true)
                }
            }
        }
    }

    fun decorateNotification(
        builder: Notification.Builder,
        artworkBitmap: Bitmap?,
        enabled: Boolean,
    ) {
        if (enabled && artworkBitmap != null) {
            runCatching {
                val palette = Palette.from(artworkBitmap).generate()
                val swatch = palette.vibrantSwatch ?: palette.dominantSwatch ?: palette.mutedSwatch
                if (swatch != null) {
                    val saturatedColor = shiftSaturation(swatch.rgb, 0.20f)
                    builder.setColor(saturatedColor)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        builder.setColorized(true)
                    }
                }
            }
        }
    }

    private fun shiftSaturation(colorInt: Int, delta: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(colorInt, hsv)
        hsv[1] = (hsv[1] + delta).coerceIn(0.0f, 1.0f)
        return Color.HSVToColor(hsv)
    }
}
