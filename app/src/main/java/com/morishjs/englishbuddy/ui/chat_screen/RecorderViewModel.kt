package com.morishjs.englishbuddy.ui.chat_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morishjs.englishbuddy.chatbot.Chatbot
import com.morishjs.englishbuddy.data.ChatMessageRepository
import com.morishjs.englishbuddy.data.RecorderRepository
import com.morishjs.englishbuddy.domain.ChatMessage
import com.morishjs.englishbuddy.domain.Role
import com.morishjs.englishbuddy.manager.TTSManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecorderViewModel @Inject internal constructor(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatbot: Chatbot,
    private val recorderRepository: RecorderRepository,
    private val ttsManager: TTSManager,
) : ViewModel() {
    private val _isStarted = MutableStateFlow(false)
    val isStarted: MutableStateFlow<Boolean> = _isStarted

    fun startRecording() {
        recorderRepository.startRecording()
        _isStarted.value = true
    }

    fun stopRecording() {
        recorderRepository.stopRecording()
        _isStarted.value = false
    }

    fun chatMessages(chatRoomId: Long) = chatMessageRepository.getChatMessages(chatRoomId)

    fun initChat(chatRoomId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            observeUserSpeak(chatRoomId)

            val hasMessages = chatMessageRepository.hasMessages(chatRoomId)
            if (!hasMessages) {
                val message = chatbot.getResponse(
                    "I want you to act as a spoken English teacher and improver. I will speak to you in English and you will reply to me in English to practice my spoken English. I want you to keep your reply neat, limiting the reply to 100 words. I want you to strictly correct my grammar mistakes, typos, and factual errors. I want you to ask me a question in your reply. Now let's start practicing, you could ask me a question first. Remember, I want you to strictly correct my grammar mistakes, typos, and factual errors."
                )

                message.content?.let { content ->
                    chatMessageRepository.saveChatMessage(
                        ChatMessage(
                            chatRoomId,
                            content,
                            role = Role.SYSTEM
                        )
                    )
                }
            }
        }
    }

    private suspend fun observeUserSpeak(chatRoomId: Long) {
        recorderRepository.transcription.collect {
            chatMessageRepository.saveChatMessage(
                ChatMessage(
                    chatRoomId,
                    it,
                    role = Role.USER
                )
            )

            chatbot.getResponse(it).content?.let { respMessage ->
                ttsManager.speak(respMessage)

                chatMessageRepository.saveChatMessage(
                    ChatMessage(
                        chatRoomId,
                        respMessage,
                        role = Role.BOT
                    )
                )
            }
        }
    }

}