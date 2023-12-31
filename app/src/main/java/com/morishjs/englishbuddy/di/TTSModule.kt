package com.morishjs.englishbuddy.di

import android.content.Context
import androidx.room.Room
import com.morishjs.englishbuddy.data.local.Database
import com.morishjs.englishbuddy.manager.TTSManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TTSModule {
    @Singleton
    @Provides
    fun provideTTSManager(@ApplicationContext context: Context): TTSManager {
        return TTSManager(context)
    }
}