package com.morishjs.englishbuddy.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.morishjs.englishbuddy.data.local.model.ChatRoomWithMessages
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatRoomDao {
    @Transaction
    @Query("SELECT * FROM chat_room ORDER BY createdAt DESC")
    fun getChatRoomList(): Flow<List<ChatRoomWithMessages>>
}