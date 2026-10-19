package com.lastwave.app.util

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.lastwave.app.data.local.AppIconTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppIconManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun switchIcon(iconTheme: AppIconTheme) {
        try {
            val darkComponent = ComponentName(context, "com.lastwave.app.MainActivityDark")
            val lightComponent = ComponentName(context, "com.lastwave.app.MainActivityLight")
            val pm = context.packageManager
            val currentDarkState = pm.getComponentEnabledSetting(darkComponent)
            val isDark = iconTheme == AppIconTheme.DARK

            if (isDark && currentDarkState == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) return
            if (!isDark && currentDarkState == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) return

            pm.setComponentEnabledSetting(
                if (isDark) darkComponent else lightComponent,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
            pm.setComponentEnabledSetting(
                if (isDark) lightComponent else darkComponent,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        } catch (e: Exception) {
            android.util.Log.e("AppIconManager", "Error safely updating launcher component", e)
        }
    }
}
