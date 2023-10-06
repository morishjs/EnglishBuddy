package com.morishjs.englishbuddy.di;

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.morishjs.englishbuddy.chatbot.Chatbot
import com.morishjs.englishbuddy.chatbot.ChatbotImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
interface ChatbotModule {

    @Binds
    @Singleton
    fun bindChatbot(chatbotImpl: ChatbotImpl): Chatbot
}