package com.morishjs.englishbuddy.data

import com.morishjs.englishbuddy.data.local.dao.ChatMessageDao
import com.morishjs.englishbuddy.data.local.model.ChatMessageEntity
import com.morishjs.englishbuddy.domain.ChatMessage
import com.morishjs.englishbuddy.domain.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatMessageRepositoryImpl @Inject constructor(
    private val chatMessageDataSource: ChatMessageDao
) : ChatMessageRepository {
    override fun saveChatMessage(message: ChatMessage) {
        chatMessageDataSource.addChatMessage(
            ChatMessageEntity(
                chatRoomId = message.chatRoomId,
                content = message.content,
                role = message.role.value
            )
        )
    }

    override fun hasMessages(chatId: Int): Boolean {
        return chatMessageDataSource.hasMessages(chatId)
    }

    override fun getChatMessages(chatId: Int): Flow<List<ChatMessage>> =
        chatMessageDataSource.getChatMessages(chatId).map {
            it.map { chatMessageEntity ->
                ChatMessage(
                    chatRoomId = chatMessageEntity.chatRoomId,
                    content = chatMessageEntity.content,
                    role = Role(chatMessageEntity.role)
                )
            }
        }
}