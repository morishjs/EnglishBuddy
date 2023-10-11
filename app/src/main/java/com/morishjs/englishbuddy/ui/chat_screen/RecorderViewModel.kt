package com.morishjs.englishbuddy.ui.chat_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morishjs.englishbuddy.chatbot.Chatbot
import com.morishjs.englishbuddy.data.ChatMessageRepository
import com.morishjs.englishbuddy.data.RecorderRepository
import com.morishjs.englishbuddy.domain.ChatMessage
import com.morishjs.englishbuddy.domain.Role
import com.morishjs.englishbuddy.manager.TTSManager
import com.morishjs.englishbuddy.speech_to_text.SpeechToText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecorderViewModel @Inject internal constructor(
    private val chatMessageRepository: ChatMessageRepository,
    private val chatbot: Chatbot,
    private val recorderRepository: RecorderRepository,
    private val speechToText: SpeechToText,
    private val ttsManager: TTSManager,
) : ViewModel() {
    private val _isStarted = MutableStateFlow(false)
    val isStarted: MutableStateFlow<Boolean> = _isStarted


    fun startRecording() {
        recorderRepository.startRecording()
        _isStarted.value = true
    }

    fun stopRecording(chatRoomId: Long) {
        val path = recorderRepository.stopRecording()

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val s = speechToText.transcript(path)

                chatMessageRepository.saveChatMessage(
                    ChatMessage(
                        chatRoomId,
                        s,
                        role = Role.USER
                    )
                )

                val responseMessage = chatbot.getResponse(s).content
                responseMessage?.let {
                    ttsManager.speak(responseMessage)

                    chatMessageRepository.saveChatMessage(
                        ChatMessage(
                            chatRoomId,
                            it,
                            role = Role.BOT
                        )
                    )
                }
            }
        }

        _isStarted.value = false
    }

    fun chatMessages(chatId: Long) = chatMessageRepository.getChatMessages(chatId)

    fun initChat(chatRoomId: Long) {
        val hasMessages = chatMessageRepository.hasMessages(chatRoomId)

        if (!hasMessages) {
            viewModelScope.launch {
                val message = chatbot.getResponse(
                    "I want you to act as a spoken English teacher and improver. I will speak to you in English and you will reply to me in English to practice my spoken English. I want you to keep your reply neat, limiting the reply to 100 words. I want you to strictly correct my grammar mistakes, typos, and factual errors. I want you to ask me a question in your reply. Now let's start practicing, you could ask me a question first. Remember, I want you to strictly correct my grammar mistakes, typos, and factual errors."
                )

                message.content?.let { content ->
                    withContext(Dispatchers.IO) {
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
    }
}