package com.morishjs.englishbuddy.di

import com.morishjs.englishbuddy.speech_to_text.SpeechToText
import com.morishjs.englishbuddy.speech_to_text.SpeechToTextImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SpeechToTextModule {
    @Binds
    @Singleton
    fun bindSpeechToText(recorderRepository: SpeechToTextImpl): SpeechToText
}