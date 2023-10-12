package com.morishjs.englishbuddy.recorder

import kotlinx.coroutines.flow.SharedFlow

interface AudioRecorder {
    fun startRecording()
    fun stopRecording()

    val transcription: SharedFlow<String>
}