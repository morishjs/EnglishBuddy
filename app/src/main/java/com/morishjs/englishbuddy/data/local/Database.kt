package com.morishjs.englishbuddy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.morishjs.englishbuddy.data.local.dao.ChatMessageDao
import com.morishjs.englishbuddy.data.local.dao.ChatRoomDao
import com.morishjs.englishbuddy.data.local.model.ChatMessageEntity
import com.morishjs.englishbuddy.data.local.model.ChatRoomEntity

@Database(
    version = 1,
    entities = [
        ChatMessageEntity::class,
        ChatRoomEntity::class,
    ]
)
abstract class Database : RoomDatabase() {
    abstract fun getChatMessageDao(): ChatMessageDao
    abstract fun getChatRoomDao(): ChatRoomDao
}