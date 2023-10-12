package com.morishjs.englishbuddy.recorder

import java.nio.file.Path

interface Recorder {
    fun startRecording()
    fun stopRecording(): Path
}