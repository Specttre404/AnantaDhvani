package com.lastwave.app.ui.visualizer

import kotlinx.coroutines.flow.MutableStateFlow

object VisualizerStateHolder {
    val isScreenOn = MutableStateFlow(true)
}
