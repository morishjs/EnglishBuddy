package com.morishjs.englishbuddy.recoder


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.file.Path
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempFile

class AudioRecorderImpl @Inject constructor() : AudioRecorder {
    private var recorder: MediaRecorder? = null
    private val silenceThreshold = 500  // This value depends on your need, might need adjustment
    private val silenceDuration = 4000L   // 4 seconds of silence
    private var lastSoundTime: Long = 0

    private var activeJob: Job? = null

    private val _isStopped = MutableStateFlow(true)
    override val isStopped: StateFlow<Boolean> get() = _isStopped

    private fun createRecorder(context: Context): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun start(context: Context): Path? =
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                10,
            )

            null
        } else {
            val tempFile = createTempFile(prefix = "tempRecording", suffix = ".mp4")

            createRecorder(context).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(tempFile.absolutePathString())

                prepare()
                start()

                recorder = this
            }

            lastSoundTime = System.currentTimeMillis()
            activeJob = CoroutineScope(Dispatchers.Default).launch {
                startCheckingAmplitude()
            }
            _isStopped.value = false

            tempFile
        }

    override fun stop() {
        activeJob?.cancel()
        activeJob = null

        recorder?.stop()
        recorder?.reset()
        recorder = null

        _isStopped.value = true
    }

    private suspend fun startCheckingAmplitude() {
        while (activeJob?.isActive === true) {
            delay(100)

            Log.d("AudioRecorder", "Amplitude: ${recorder?.maxAmplitude ?: 0}")

            if ((recorder?.maxAmplitude ?: 0) > silenceThreshold) {
                lastSoundTime = System.currentTimeMillis()
            }

            if (System.currentTimeMillis() - lastSoundTime > silenceDuration) {
                withContext(Dispatchers.Main) {
                    stop()
                }
                break
            }
        }
    }
}