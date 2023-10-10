package com.morishjs.englishbuddy.speech_to_text

import java.nio.file.Path

interface SpeechToText {
    suspend fun transcript(path: Path): String
}