package com.morishjs.englishbuddy.di;

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(ViewModelComponent::class)
object OpenAIModule {

    @Provides
    fun bindOpenAI(): OpenAI {
        return OpenAI(
            token = "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",
            timeout = Timeout(socket = 60.seconds),
        )
    }
}