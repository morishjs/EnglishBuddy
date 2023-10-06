package com.morishjs.englishbuddy.data

import com.morishjs.englishbuddy.data.local.dao.ChatMessageDao
import com.morishjs.englishbuddy.data.local.model.ChatMessageEntity
import com.morishjs.englishbuddy.domain.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatMessageRepositoryImpl @Inject constructor(
    private val chatMessageDataSource: ChatMessageDao
) : ChatMessageRepository {
    override fun saveChatMessage(message: ChatMessage) {
        chatMessageDataSource.addChatMessage(
            ChatMessageEntity(
                chatId = message.chatId,
                content = message.content,
            )
        )
    }

    override fun getChatMessages(chatId: Int): Flow<List<ChatMessage>> =
        chatMessageDataSource.getChatMessages(chatId).map {
            it.map { chatMessageEntity ->
                ChatMessage(
                    chatId = chatMessageEntity.chatId,
                    content = chatMessageEntity.content,
                )
            }
        }
}