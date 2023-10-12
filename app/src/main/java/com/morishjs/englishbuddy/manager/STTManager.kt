package com.morishjs.englishbuddy.manager

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.vosk.LibVosk
import org.vosk.LogLevel
import org.vosk.Model
import org.vosk.Recognizer
import org.vosk.android.RecognitionListener
import org.vosk.android.SpeechService
import org.vosk.android.StorageService
import java.io.IOException


class STTManager(private val context: Context) : RecognitionListener {
    private var job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.IO)

    private val STATE_START = 0
    private val STATE_READY = 1
    private val STATE_DONE = 2
    private val STATE_FILE = 3
    private val STATE_MIC = 4

    private val _transcription =
        MutableSharedFlow<String>(1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val transcription = _transcription.asSharedFlow()

    private var model: Model? = null
    private var speechService: SpeechService? = null

    fun initialize() {
        LibVosk.setLogLevel(LogLevel.INFO);

        StorageService.unpack(context, "model-en-us", "model",
            {
                model = it
//                setUiState(STATE_READY)
            },
            { exception: IOException ->
                Log.e("STTManager", "Failed to unpack the model", exception)
//                setErrorState(
//                    "Failed to unpack the model" + exception.message
//                )
            })
    }

    fun start() {
        if (speechService != null) {
//            setUiState(STATE_DONE)
            speechService!!.stop()
            speechService = null
        } else {
//            setUiState(STATE_MIC)
            try {
                val rec = Recognizer(model, 16000.0f)
                speechService = SpeechService(rec, 16000.0f)
                speechService!!.startListening(this)
            } catch (e: IOException) {
//                setErrorState(e.message)
            }
        }
    }

    fun stop() {
        if (speechService != null) {
            speechService!!.stop()
            speechService = null
        }
    }

    fun destroy() {
        stop()
        job.cancel()
    }

    override fun onPartialResult(hypothesis: String?) {
        hypothesis?.let {
            Log.d("STTManager", it)
        }
    }

    override fun onResult(hypothesis: String?) {
//        hypothesis?.let {
//            scope.launch {
//                _transcription.emit(it)
//            }
//        }
    }

    override fun onFinalResult(hypothesis: String?) {
        hypothesis?.let {
            scope.launch {
                _transcription.emit(it)
            }
        }
    }

    override fun onError(exception: Exception?) {
        Log.e("STTManager", exception.toString())
    }

    override fun onTimeout() {
        Log.e("STTManager", "onTimeout")
    }
}