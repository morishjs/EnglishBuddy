package com.morishjs.englishbuddy.ui.chat_rooms

import androidx.lifecycle.ViewModel
import com.morishjs.englishbuddy.data.ChatRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ChatRoomsViewModel @Inject internal constructor(
    private val chatMessageRepository: ChatRoomRepository
): ViewModel() {
    val chatRooms get() = chatMessageRepository.getChatRoomList()
}