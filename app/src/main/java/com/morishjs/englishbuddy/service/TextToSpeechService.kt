package com.morishjs.englishbuddy.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TextToSpeechService : Service() {
    private lateinit var textToSpeech: TextToSpeech

    companion object {
        const val ACTION_START = "TTS_START"
        const val ACTION_INIT = "TTS_INIT"
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_INIT -> {
                initTTS()
            }

            ACTION_START -> {
                // Start recording
                val text = intent.getStringExtra("text")
                text?.let {
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    private fun initTTS() {
        textToSpeech = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.US
            } else {
                Log.e("TTS", "TTS ERROR")
            }
        }
    }
}