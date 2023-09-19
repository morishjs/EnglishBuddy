package com.morishjs.englishbuddy.recoder

import android.content.Context

interface AudioRecorder {
    fun stop()
    fun start(context: Context)
}