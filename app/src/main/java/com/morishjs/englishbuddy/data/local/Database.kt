package com.morishjs.englishbuddy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.morishjs.englishbuddy.data.local.dao.ChatMessageDao
import com.morishjs.englishbuddy.data.local.model.ChatMessageEntity

@Database(
    version = 1,
    entities = [
        ChatMessageEntity::class
    ]
)
abstract class Database:RoomDatabase() {
    abstract fun getChatMessageDao(): ChatMessageDao
}