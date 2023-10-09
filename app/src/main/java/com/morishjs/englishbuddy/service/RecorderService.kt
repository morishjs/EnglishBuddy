package com.morishjs.englishbuddy.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.morishjs.englishbuddy.chatbot.Chatbot
import com.morishjs.englishbuddy.data.ChatMessageRepository
import com.morishjs.englishbuddy.data.RecorderRepository
import com.morishjs.englishbuddy.domain.ChatMessage
import com.morishjs.englishbuddy.domain.Recorder
import com.morishjs.englishbuddy.domain.Role
import com.morishjs.englishbuddy.speechtotext.SpeechToText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class RecorderService : Service() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val silenceThreshold = 500  // This value depends on your need, might need adjustment
    private val silenceDuration = 4000L   // 4 seconds of silence

    private var lastSoundTime: Long = 0

    private var recorder: Recorder? = null

    @Inject
    lateinit var recorderRepository: RecorderRepository

    @Inject
    lateinit var speechToText: SpeechToText

    @Inject
    lateinit var chatbot: Chatbot

    @Inject
    lateinit var chatMessageRepository: ChatMessageRepository

    companion object {
        const val ACTION_START = "RECORD_START"
        const val ACTION_STOP = "RECORD_STOP"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                // Start recording
                startRecorder()
            }

            ACTION_STOP -> {
                // Stop recording
                stopRecorder()
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRecorder() {
        recorder = recorderRepository.startRecording(this)
        lastSoundTime = System.currentTimeMillis()

        scope.launch {
            startCheckingAmplitude()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun stopRecorder() {
        val path = recorderRepository.stopRecording()

        scope.launch {
            val s = speechToText.transcript(path)

            scope.launch {
                chatMessageRepository.saveChatMessage(
                    ChatMessage(
                        0,
                        s,
                        role = Role.USER
                    )
                )
            }

            val responseMessage = chatbot.getResponse(s).content
            responseMessage?.let {
                Intent(this@RecorderService, TextToSpeechService::class.java)
                    .apply {
                        chatMessageRepository.saveChatMessage(
                            ChatMessage(
                                0,
                                it,
                                role = Role.BOT
                            )
                        )

                        action = TextToSpeechService.ACTION_START
                        putExtra("text", it)
                    }.also {
                        startForegroundService(it)
                    }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun startCheckingAmplitude() {
        while (job.isActive) {
            delay(100)

//            Log.d("AudioRecorder", "Amplitude: ${recorder?.maxAmplitude ?: 0}")
            val t = recorder?.maxAmplitude()
            if ((t ?: 0) > silenceThreshold) {
                lastSoundTime = System.currentTimeMillis()
            }

            if (System.currentTimeMillis() - lastSoundTime > silenceDuration) {
                stopRecorder()
                break
            }
        }
    }
}
