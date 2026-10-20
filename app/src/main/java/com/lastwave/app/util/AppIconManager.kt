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
            val isDark = iconTheme == AppIconTheme.DARK
            val targetEnable = if (isDark) darkComponent else lightComponent
            val targetDisable = if (isDark) lightComponent else darkComponent

            if (pm.getComponentEnabledSetting(targetEnable) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                return
            }

            pm.setComponentEnabledSetting(
                targetEnable,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
            pm.setComponentEnabledSetting(
                targetDisable,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        } catch (e: Exception) {
            android.util.Log.e("AppIconManager", "Safe icon switch error", e)
        }
    }
}
