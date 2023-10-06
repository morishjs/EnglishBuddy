package com.morishjs.englishbuddy.data

import com.morishjs.englishbuddy.domain.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatMessageRepository {
    fun saveChatMessage(message: ChatMessage)
    fun getChatMessages(chatId: Int): Flow<List<ChatMessage>>
    fun hasMessages(chatId: Int): Boolean
}