package com.morishjs.englishbuddy.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_room")
data class ChatRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val createdAt: Long,
)