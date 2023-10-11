package com.morishjs.englishbuddy.data

import com.morishjs.englishbuddy.data.local.dao.ChatMessageDao
import com.morishjs.englishbuddy.data.local.model.ChatMessageEntity
import com.morishjs.englishbuddy.domain.ChatMessage
import com.morishjs.englishbuddy.domain.ChatRoom
import com.morishjs.englishbuddy.domain.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


typealias ChatRoomId = Long

class ChatMessageRepositoryImpl @Inject constructor(
    private val chatMessageDataSource: ChatMessageDao
) : ChatMessageRepository {
    override suspend fun saveChatMessage(message: ChatMessage) {
        chatMessageDataSource.addChatMessage(
            ChatMessageEntity(
                chatRoomId = message.chatRoomId,
                content = message.content,
                role = message.role.value
            )
        )
    }

    override fun hasMessages(chatRoomId: ChatRoomId): Boolean {
        return chatMessageDataSource.hasMessages(chatRoomId)
    }

    override fun getChatMessages(chatRoomId: ChatRoomId): Flow<List<ChatMessage>> =
        chatMessageDataSource.getChatMessages(chatRoomId).map {
            it.map { chatMessageEntity ->
                ChatMessage(
                    chatRoomId = chatMessageEntity.chatRoomId,
                    content = chatMessageEntity.content,
                    role = Role(chatMessageEntity.role)
                )
            }
        }
}