package com.morishjs.englishbuddy.domain

data class ChatRoomWithMessage(
    val id: Int,
    val latestMessage: ChatMessage,
    val createdAt: String,
)
