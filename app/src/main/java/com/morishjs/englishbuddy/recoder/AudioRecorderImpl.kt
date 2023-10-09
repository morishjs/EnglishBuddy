package com.morishjs.englishbuddy.recoder


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.morishjs.englishbuddy.domain.Recorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempFile

class AudioRecorderImpl @Inject constructor() : AudioRecorder {
    private var recorder: MediaRecorder? = null

    private var activeJob: Job? = null
    private var path: Path? = null

    private val _isStopped = MutableStateFlow(true)
    override val isStopped: StateFlow<Boolean> get() = _isStopped

    private fun createRecorder(context: Context): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun start(context: Context): Recorder? {
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

            return null
        } else {
            val tempFile = createTempFile(prefix = "tempRecording", suffix = ".mp4")

            createRecorder(context).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(tempFile.absolutePathString())

                prepare()
                start()

                recorder = this
            }

            activeJob = CoroutineScope(Dispatchers.Default).launch {
//                startCheckingAmplitude()
            }
            _isStopped.value = false
            path = tempFile

            return recorder?.let {
                Recorder(it)
            }
        }
    }

    override fun stop(): Path {
        activeJob?.cancel()
        activeJob = null

        recorder?.stop()
        recorder?.reset()
        recorder = null

        _isStopped.value = true

        return path!!
    }
}