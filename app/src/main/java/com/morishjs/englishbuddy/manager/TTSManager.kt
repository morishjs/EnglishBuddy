package com.morishjs.englishbuddy.manager

import android.content.Context
import android.speech.tts.TextToSpeech

class TTSManager(context: Context) {
    private lateinit var textToSpeech: TextToSpeech

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.language = java.util.Locale.US
            } else {
                android.util.Log.e("TTS", "TTS ERROR")
            }
        }
    }

    fun speak(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }
}