package com.morishjs.englishbuddy.di

import com.morishjs.englishbuddy.recoder.AudioRecorder
import com.morishjs.englishbuddy.recoder.AudioRecorderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AudioRecorderModule {

    @Binds
    abstract fun bindAnalyticsService(
        analyticsServiceImpl: AudioRecorderImpl
    ): AudioRecorder
}