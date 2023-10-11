package com.morishjs.englishbuddy.ui.chat_rooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morishjs.englishbuddy.data.ChatRoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ChatRoomsViewModel @Inject internal constructor(
    private val chatRoomRepository: ChatRoomRepository
): ViewModel() {
    fun createChatRoom(): Deferred<Long> {
        return viewModelScope.async {
            withContext(Dispatchers.IO) {
                chatRoomRepository.createChatRoom()
            }
        }
    }

    val chatRooms get() = chatRoomRepository.getChatRoomList()
}