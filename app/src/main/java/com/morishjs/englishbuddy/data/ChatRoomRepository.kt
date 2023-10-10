package com.morishjs.englishbuddy.data

import com.morishjs.englishbuddy.data.local.model.ChatRoomWithMessages
import com.morishjs.englishbuddy.domain.ChatRoomWithMessage
import kotlinx.coroutines.flow.Flow


interface ChatRoomRepository {
    fun getChatRoomList(): Flow<List<ChatRoomWithMessage>>
}