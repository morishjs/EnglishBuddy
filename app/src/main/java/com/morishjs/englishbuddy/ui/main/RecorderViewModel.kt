package com.morishjs.englishbuddy.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.audio.TranscriptionRequest
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.morishjs.englishbuddy.data.RecorderRepositoryImpl
import com.morishjs.englishbuddy.service.RecorderService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.source
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.name

@HiltViewModel
class RecorderViewModel @Inject internal constructor(
    private val recorderRepository: RecorderRepositoryImpl,
) : ViewModel() {
    private val _isStarted = MutableStateFlow(false)
    val isStarted: MutableStateFlow<Boolean> = _isStarted

    private val _transcript = MutableStateFlow("")
    val transcript: StateFlow<String> = _transcript

    private val _responseMessage = MutableStateFlow("")
    val responseMessage: StateFlow<String> = _responseMessage

    private val chatMessages = mutableListOf(
        ChatMessage(
            role = ChatRole.System,
            content = "I want you to act as a spoken English teacher and improver. I will speak to you in English and you will reply to me in English to practice my spoken English. I want you to keep your reply neat, limiting the reply to 100 words. I want you to strictly correct my grammar mistakes, typos, and factual errors. I want you to ask me a question in your reply. Now let's start practicing, you could ask me a question first. Remember, I want you to strictly correct my grammar mistakes, typos, and factual errors.",
        )
    )

    init {
        observeRecordingStop()
    }

    fun startRecording(context: Context) {
//        path = recorderRepository.startRecording(context) ?: return
        Intent(context, RecorderService::class.java).apply {
            action = RecorderService.ACTION_START
        }.also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(it)
            } else {
                context.startService(it)
            }
        }

        _isStarted.value = true
    }

    fun stopRecording(context: Context) {
        val intent = Intent(context, RecorderService::class.java).apply {
            action = RecorderService.ACTION_STOP
        }
        context.startService(intent)

        _isStarted.value = false
    }

    private fun observeRecordingStop() {
        viewModelScope.launch {
            recorderRepository.isStopped.collect { stopped ->
                _isStarted.value = !stopped
            }
        }
    }
}