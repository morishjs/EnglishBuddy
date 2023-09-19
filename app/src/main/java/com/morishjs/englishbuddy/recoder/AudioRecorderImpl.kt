package com.morishjs.englishbuddy.recoder


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempFile

class AudioRecorderImpl @Inject constructor(
    @ActivityContext private val context: Context
) : AudioRecorder {
    private var recorder: MediaRecorder? = null

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun start() {
        val tempFile = createTempFile(prefix = "tempRecording", suffix = ".mp4")
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                10,
            );
        } else {
            createRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(tempFile.absolutePathString())

                prepare()
                start()

                recorder = this
            }
        }
    }

    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }
}