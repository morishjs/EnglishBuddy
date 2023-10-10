package com.morishjs.englishbuddy.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class ChatRoomWithMessages(
    @Embedded val chatRoom: ChatRoomEntity,
    @Relation(parentColumn = "id", entityColumn = "chatRoomId")
    val chatMessages: List<ChatMessageEntity>
)
