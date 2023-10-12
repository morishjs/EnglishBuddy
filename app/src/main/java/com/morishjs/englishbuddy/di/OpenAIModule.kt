package com.morishjs.englishbuddy.di;

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.morishjs.englishbuddy.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
object OpenAIModule {
    @Provides
    @Singleton
    fun bindOpenAI(): OpenAI {
        return OpenAI(
            token = BuildConfig.OPENAI_API_KEY,
            timeout = Timeout(socket = 60.seconds),
        )
    }
}