package com.morishjs.englishbuddy.domain

@JvmInline
value class Role(val value: String) {
    companion object {
        val USER = Role("user")
        val SYSTEM = Role("system")
        val BOT = Role("bot")
    }
}

data class ChatMessage(
    val chatRoomId: Int,
    val content: String,
    val role: Role
)