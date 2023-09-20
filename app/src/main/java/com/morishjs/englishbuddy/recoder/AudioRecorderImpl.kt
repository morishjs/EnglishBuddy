package com.morishjs.englishbuddy.recoder


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ActivityContext
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempFile

class AudioRecorderImpl @Inject constructor() : AudioRecorder {
    private var recorder: MediaRecorder? = null

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

            tempFile
        }

    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }
}