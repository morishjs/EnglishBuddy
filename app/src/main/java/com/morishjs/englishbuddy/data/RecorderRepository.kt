package com.morishjs.englishbuddy.data

import android.content.Context
import android.media.MediaRecorder
import com.morishjs.englishbuddy.domain.Recorder
import kotlinx.coroutines.flow.SharedFlow
import java.nio.file.Path

interface RecorderRepository {
    fun startRecording()
    fun stopRecording()

    val transcription: SharedFlow<String>
}