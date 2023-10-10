package com.morishjs.englishbuddy.data

import android.annotation.SuppressLint
import com.morishjs.englishbuddy.data.local.dao.ChatRoomDao
import com.morishjs.englishbuddy.domain.ChatMessage
import com.morishjs.englishbuddy.domain.ChatRoomWithMessage
import com.morishjs.englishbuddy.domain.Role
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import javax.inject.Inject

class ChatRoomRepositoryImpl @Inject constructor(
    private val chatRoomDataSource: ChatRoomDao
) : ChatRoomRepository {
    @SuppressLint("SimpleDateFormat")
    override fun getChatRoomList() = chatRoomDataSource.getChatRoomList().map { it ->
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        it.mapNotNull { chatRoomWithMessages ->
            val lastMessage = chatRoomWithMessages.chatMessages.lastOrNull()
            if (lastMessage == null || lastMessage.role == Role.SYSTEM.value) return@mapNotNull null

            ChatRoomWithMessage(
                id = chatRoomWithMessages.chatRoom.id!!,
                createdAt = formatter.format(chatRoomWithMessages.chatRoom.createdAt),
                latestMessage = ChatMessage(
                    chatRoomId = lastMessage.chatRoomId,
                    content = lastMessage.content,
                    role = Role(lastMessage.role),
                ),
            )
        }
    }
}