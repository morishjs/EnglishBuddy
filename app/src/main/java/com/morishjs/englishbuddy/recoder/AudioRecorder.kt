package com.morishjs.englishbuddy.recoder

import android.content.Context
import android.media.MediaRecorder
import com.morishjs.englishbuddy.domain.Recorder
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.Path

interface AudioRecorder {
    val isStopped: StateFlow<Boolean>
    fun stop(): Path
    fun start(context: Context): Recorder?
}