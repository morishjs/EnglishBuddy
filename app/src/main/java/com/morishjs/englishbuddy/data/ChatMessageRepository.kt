package com.morishjs.englishbuddy.data

import com.morishjs.englishbuddy.domain.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatMessageRepository {
    suspend fun saveChatMessage(message: ChatMessage)
    fun getChatMessages(chatRoomId: Long): Flow<List<ChatMessage>>
    fun hasMessages(chatRoomId: Long): Boolean
}