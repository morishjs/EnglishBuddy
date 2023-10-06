package com.morishjs.englishbuddy.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morishjs.englishbuddy.chatbot.Chatbot
import com.morishjs.englishbuddy.data.ChatMessageRepository
import com.morishjs.englishbuddy.domain.Role
import com.morishjs.englishbuddy.service.RecorderService
import com.morishjs.englishbuddy.service.TextToSpeechService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecorderViewModel @Inject internal constructor(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatbot: Chatbot
) : ViewModel() {
    private val _isStarted = MutableStateFlow(false)
    val isStarted: MutableStateFlow<Boolean> = _isStarted

    @RequiresApi(Build.VERSION_CODES.O)
    fun startRecording(context: Context) {
        Intent(context, RecorderService::class.java).apply {
            action = RecorderService.ACTION_START
        }.also {
            context.startForegroundService(it)
        }

        _isStarted.value = true
    }

    fun stopRecording(context: Context) {
        val intent = Intent(context, RecorderService::class.java).apply {
            action = RecorderService.ACTION_STOP
        }
        context.startService(intent)

        _isStarted.value = false
    }

    fun chatMessages(chatId: Int) = chatMessageRepository.getChatMessages(chatId)

    fun initChat(context: Context) {
        val hasMessages = chatMessageRepository.hasMessages(0)
        if (!hasMessages) {
            viewModelScope.launch {
                val message = chatbot.getResponse(
                    "I want you to act as a spoken English teacher and improver. I will speak to you in English and you will reply to me in English to practice my spoken English. I want you to keep your reply neat, limiting the reply to 100 words. I want you to strictly correct my grammar mistakes, typos, and factual errors. I want you to ask me a question in your reply. Now let's start practicing, you could ask me a question first. Remember, I want you to strictly correct my grammar mistakes, typos, and factual errors."
                )
                message.content?.let { content ->
                    Intent(context, TextToSpeechService::class.java)
                        .apply {
                            withContext(Dispatchers.IO) {
                                chatMessageRepository.saveChatMessage(
                                    com.morishjs.englishbuddy.domain.ChatMessage(
                                        0,
                                        content,
                                        role = Role.SYSTEM
                                    )
                                )
                            }

                            action = TextToSpeechService.ACTION_START
                            putExtra("text", content)
                        }
                        .also {
                            startForegroundService(context, it)
                        }
                }
            }
        }
    }
}