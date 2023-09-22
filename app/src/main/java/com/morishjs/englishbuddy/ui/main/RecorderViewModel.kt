package com.morishjs.englishbuddy.ui.main

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.audio.TranscriptionRequest
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.morishjs.englishbuddy.recoder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.source
import java.nio.file.Path
import java.util.Locale
import javax.inject.Inject
import kotlin.io.path.name

@HiltViewModel
class RecorderViewModel @Inject internal constructor(
    private val recorder: AudioRecorder,
    private val openAI: OpenAI
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

    var path: Path? = null

    private lateinit var textToSpeech: TextToSpeech

    init {
        observeRecordingStop()
        observeTranscript()
    }

    private fun observeTranscript() {
        viewModelScope.launch {
            transcript.collect { text ->
                val chatCompletionRequest = ChatCompletionRequest(
                    model = ModelId("gpt-3.5-turbo"),
                    n = 1,
                    messages = chatMessages + listOf(
                        ChatMessage(
                            role = ChatRole.User,
                            content = text,
                        )
                    )
                )

                val responseMessage = openAI.chatCompletion(chatCompletionRequest)
                    .choices
                    .map {
                        it.message
                    }
                    .first()
                chatMessages.add(responseMessage)

                responseMessage.content?.let {
                    _responseMessage.value = it
                }
            }
        }
    }

    fun speakText(text: String, context: Context) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.US
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                Log.e("TTS", "TTS ERROR")
            }
        }
    }

    fun start(context: Context) {
        path = recorder.start(context) ?: return
        _isStarted.value = true
    }

    fun stop() {
        recorder.stop()

        updateTranscript()
        _isStarted.value = false
    }

    private suspend fun transcript(path: Path): String {
        val request = TranscriptionRequest(
            audio = FileSource(
                name = path.name,
                source = path.source()
            ),
            model = ModelId("whisper-1"),
            language = "en",
        )

        val response = openAI.transcription(request)
        return response.text
    }

    private fun updateTranscript() {
        path?.let {
            viewModelScope.launch {
                _transcript.value = transcript(it)
            }
        }
    }

    private fun observeRecordingStop() {
        viewModelScope.launch {
            recorder.isStopped.collect { stopped ->
                if (stopped) {
                    updateTranscript()
                    _isStarted.value = false
                } else {
                    _isStarted.value = true
                }
            }
        }
    }
}