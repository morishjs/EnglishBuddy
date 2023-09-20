package com.morishjs.englishbuddy.recoder

import android.content.Context
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.Path

interface AudioRecorder {
    val isStopped: StateFlow<Boolean>
    fun stop()
    fun start(context: Context): Path?
}