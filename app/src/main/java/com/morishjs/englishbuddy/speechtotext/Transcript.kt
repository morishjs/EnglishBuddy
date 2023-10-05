package com.morishjs.englishbuddy.speechtotext

import java.nio.file.Path

interface SpeechToText {
    suspend fun transcript(path: Path): String
}