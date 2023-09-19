package com.morishjs.englishbuddy.di

import com.morishjs.englishbuddy.recoder.AudioRecorder
import com.morishjs.englishbuddy.recoder.AudioRecorderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class AudioRecorderModule {

    @Binds
    abstract fun bindAnalyticsService(
        analyticsServiceImpl: AudioRecorderImpl
    ): AudioRecorder
}