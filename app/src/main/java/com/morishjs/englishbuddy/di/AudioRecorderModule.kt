package com.morishjs.englishbuddy.di

import com.morishjs.englishbuddy.recorder.AudioRecorder
import com.morishjs.englishbuddy.recorder.AudioRecorderImpl
import com.morishjs.englishbuddy.recorder.Recorder
import com.morishjs.englishbuddy.recorder.RecorderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AudioRecorderModule {

    @Binds
    @Singleton
    fun bindAudioRecorder(impl: AudioRecorderImpl): AudioRecorder

    @Binds
    @Singleton
    fun bindRecorder(impl: RecorderImpl): Recorder
}