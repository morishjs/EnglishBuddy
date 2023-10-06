package com.morishjs.englishbuddy.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.morishjs.englishbuddy.chatbot.Chatbot
import com.morishjs.englishbuddy.data.ChatMessageRepository
import com.morishjs.englishbuddy.data.RecorderRepository
import com.morishjs.englishbuddy.domain.ChatMessage
import com.morishjs.englishbuddy.domain.Role
import com.morishjs.englishbuddy.speechtotext.SpeechToText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.nio.file.Path
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class RecorderService : Service() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var recorderRepository: RecorderRepository

    @Inject
    lateinit var speechToText: SpeechToText

    @Inject
    lateinit var chatbot: Chatbot

    @Inject
    lateinit var chatMessageRepository: ChatMessageRepository

    var path: Path? = null

    companion object {
        const val ACTION_START = "RECORD_START"
        const val ACTION_STOP = "RECORD_STOP"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                // Start recording
                path = recorderRepository.startRecording(this)
            }

            ACTION_STOP -> {
                // Stop recording
                path?.let {
                    scope.launch {
                        val s = speechToText.transcript(it)

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
                                    startService(it)
                                }
                        }
                    }
                }

                recorderRepository.stopRecording()
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
}
