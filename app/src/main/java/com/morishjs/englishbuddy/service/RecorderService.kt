package com.morishjs.englishbuddy.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.morishjs.englishbuddy.data.RecorderRepository
import com.morishjs.englishbuddy.speechtotext.SpeechToText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.nio.file.Path
import javax.inject.Inject

@AndroidEntryPoint
class RecorderService: Service() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var recorderRepository: RecorderRepository

    @Inject
    lateinit var speechToText: SpeechToText

    var path: Path? = null

    companion object {
        const val ACTION_START = "RECORD_START"
        const val ACTION_STOP = "RECORD_STOP"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("RecorderService", "onCreate called")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("RecorderService", "onStartCommand called")

        when(intent?.action) {
            ACTION_START -> {
                // Start recording
                path = recorderRepository.startRecording(this)
            }
            ACTION_STOP -> {
                // Stop recording
                path?.let {
                    scope.launch {
                        val s = speechToText.transcript(it)
                        Log.d("RecorderService", s)
                    }
                }

                recorderRepository.stopRecording()
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
