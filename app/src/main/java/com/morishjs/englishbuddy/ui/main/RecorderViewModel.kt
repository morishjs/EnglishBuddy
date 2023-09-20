package com.morishjs.englishbuddy.ui.main

import android.content.Context
import android.media.MediaRecorder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.audio.TranscriptionRequest
import com.aallam.openai.api.audio.TranscriptionRequestBuilder
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.file.FileSourceBuilder
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.morishjs.englishbuddy.recoder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okio.FileSystem
import okio.FileSystem.Companion.RESOURCES
import okio.FileSystem.Companion.asOkioFileSystem
import okio.Path.Companion.toOkioPath
import okio.source
import java.nio.file.Path
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
    val transcript: MutableStateFlow<String> = _transcript

    var path: Path? = null

    fun start(context: Context) {
        path = recorder.start(context) ?: return
        _isStarted.value = true
    }

    fun stop() {
        recorder.stop()

        path?.let {
            viewModelScope.launch {
                _transcript.value = transcript(it)
            }
        }

        _isStarted.value = false
    }

    private suspend fun transcript(path: Path): String {
        val request = TranscriptionRequest(
            audio = FileSource(
                name = path.name,
                source = path.source()
            ),
            model = ModelId("whisper-1"),
        )

        val response = openAI.transcription(request)
        return response.text
    }
}