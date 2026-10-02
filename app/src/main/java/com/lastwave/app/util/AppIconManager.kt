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
        val darkComponent = ComponentName(context, "com.lastwave.app.MainActivityDark")
        val lightComponent = ComponentName(context, "com.lastwave.app.MainActivityLight")

        val pm = context.packageManager
        when (iconTheme) {
            AppIconTheme.DARK -> {
                pm.setComponentEnabledSetting(
                    darkComponent,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP,
                )
                pm.setComponentEnabledSetting(
                    lightComponent,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP,
                )
            }
            AppIconTheme.LIGHT -> {
                pm.setComponentEnabledSetting(
                    lightComponent,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP,
                )
                pm.setComponentEnabledSetting(
                    darkComponent,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP,
                )
            }
        }
    }
}
