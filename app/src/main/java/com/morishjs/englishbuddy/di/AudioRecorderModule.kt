package com.morishjs.englishbuddy.di

import android.content.Context
import com.morishjs.englishbuddy.annotations.RecorderVoskClient
import com.morishjs.englishbuddy.recorder.AudioRecorder
import com.morishjs.englishbuddy.recorder.VoskRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AudioRecorderModule {
    @Provides
    @RecorderVoskClient
    fun bindVoskRecorder(
        @ApplicationContext context: Context,
    ): AudioRecorder {
        return VoskRecorder(context).apply {
            initialize()
        }
    }
}