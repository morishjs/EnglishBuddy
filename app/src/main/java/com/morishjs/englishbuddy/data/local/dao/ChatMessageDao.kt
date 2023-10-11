package com.morishjs.englishbuddy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.morishjs.englishbuddy.data.local.model.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_message WHERE chatRoomId = :chatRoomId ORDER BY id ASC")
    fun getChatMessages(chatRoomId: Long): Flow<List<ChatMessageEntity>>

    @Insert
    suspend fun addChatMessage(chatMessage: ChatMessageEntity)

    @Query("SELECT EXISTS(SELECT * FROM chat_message WHERE chatRoomId = :chatRoomId)")
    fun hasMessages(chatRoomId: Long): Boolean
}