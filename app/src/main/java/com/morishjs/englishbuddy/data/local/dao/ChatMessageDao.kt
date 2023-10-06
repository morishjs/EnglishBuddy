package com.morishjs.englishbuddy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.morishjs.englishbuddy.data.local.model.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_message WHERE chatId = :chatId ORDER BY id ASC")
    fun getChatMessages(chatId: Int): Flow<List<ChatMessageEntity>>

    @Insert
    fun addChatMessage(chatMessage: ChatMessageEntity)
}