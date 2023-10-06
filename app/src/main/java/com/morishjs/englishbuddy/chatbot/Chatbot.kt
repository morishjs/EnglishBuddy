package com.morishjs.englishbuddy.chatbot

import com.aallam.openai.api.chat.ChatMessage

interface Chatbot {
    suspend fun getResponse(str: String, chatMessageContext: List<ChatMessage>? = null): List<ChatMessage>
}