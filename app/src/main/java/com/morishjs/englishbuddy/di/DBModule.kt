package com.morishjs.englishbuddy.di

import android.content.Context
import androidx.room.Room
import com.morishjs.englishbuddy.data.local.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DBModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "english_buddy"
        ).build()
    }

    @Provides
    @Singleton
    fun bindChatMessageDao(appDatabase: Database) = appDatabase.getChatMessageDao()
}