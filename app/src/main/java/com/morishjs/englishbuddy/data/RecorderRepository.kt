package com.morishjs.englishbuddy.data

import android.content.Context
import java.nio.file.Path

interface RecorderRepository {
    fun startRecording(context: Context)
    fun stopRecording(): Path
}