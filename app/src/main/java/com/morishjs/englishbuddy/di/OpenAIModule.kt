package com.morishjs.englishbuddy.di;

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
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
            token = "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",
            timeout = Timeout(socket = 60.seconds),
        )
    }
}