package com.morishjs.englishbuddy.recorder

import android.content.Context
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.Path

interface AudioRecorder {
    val isStopped: StateFlow<Boolean>
    fun stop(): Path
    fun start(context: Context)
}