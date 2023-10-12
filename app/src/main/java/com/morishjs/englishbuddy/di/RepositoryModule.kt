package com.morishjs.englishbuddy.di

import com.morishjs.englishbuddy.data.ChatMessageRepository
import com.morishjs.englishbuddy.data.ChatMessageRepositoryImpl
import com.morishjs.englishbuddy.data.ChatRoomRepository
import com.morishjs.englishbuddy.data.ChatRoomRepositoryImpl
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
    fun bindChatMessageRepository(chatMessageRepository: ChatMessageRepositoryImpl): ChatMessageRepository

    @Binds
    @Singleton
    fun bindChatRoomRepository(chatRoomRepository: ChatRoomRepositoryImpl): ChatRoomRepository
}