package com.morishjs.englishbuddy.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.morishjs.englishbuddy.data.RecorderRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.io.path.absolutePathString

@AndroidEntryPoint
class RecorderService: Service() {
    @Inject
    lateinit var recorderRepository: RecorderRepository

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
                val path = recorderRepository.startRecording(this)
                path?.let {
                    Log.d("Path", it.absolutePathString())
                }
            }
            ACTION_STOP -> {
                // Stop recording
                recorderRepository.stopRecording()
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}
