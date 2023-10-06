package com.morishjs.englishbuddy.di

import com.morishjs.englishbuddy.data.ChatMessageRepository
import com.morishjs.englishbuddy.data.ChatMessageRepositoryImpl
import com.morishjs.englishbuddy.data.RecorderRepository
import com.morishjs.englishbuddy.data.RecorderRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindRecorderRepository(recorderRepository: RecorderRepositoryImpl): RecorderRepository

    @Binds
    @Singleton
    fun bindChatMessageRepository(chatMessageRepository: ChatMessageRepositoryImpl): ChatMessageRepository
}