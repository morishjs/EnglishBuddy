package com.morishjs.englishbuddy.data

import android.content.Context
import android.media.MediaRecorder
import com.morishjs.englishbuddy.domain.Recorder
import java.nio.file.Path

interface RecorderRepository {
    fun startRecording(): Recorder?
    fun stopRecording(): Path
}