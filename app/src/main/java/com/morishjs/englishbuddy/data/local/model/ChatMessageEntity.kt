package com.morishjs.englishbuddy.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_message")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val chatRoomId: Long,
    val content: String,
    val role: String,
)