package com.morishjs.englishbuddy.chatbot

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import javax.inject.Inject

class ChatbotImpl @Inject constructor(
    private val openAI: OpenAI
) : Chatbot {
    override suspend fun getResponse(
        str: String,
        chatMessageContext: List<ChatMessage>?
    ): ChatMessage {
        val chatMessages = ((chatMessageContext ?: listOf()) + listOf(
            ChatMessage(
                role = ChatRole.User,
                content = str,
            )
        )).toMutableList()

        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            n = 1,
            messages = chatMessages,
            maxTokens = 150,
        )

        val responseMessage = openAI.chatCompletion(chatCompletionRequest)
            .choices
            .map {
                it.message
            }
            .first()

        return responseMessage
    }
}