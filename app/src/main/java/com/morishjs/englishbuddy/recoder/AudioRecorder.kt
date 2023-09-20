package com.morishjs.englishbuddy.recoder

import android.content.Context
import java.nio.file.Path

interface AudioRecorder {
    fun stop()
    fun start(context: Context): Path?
}