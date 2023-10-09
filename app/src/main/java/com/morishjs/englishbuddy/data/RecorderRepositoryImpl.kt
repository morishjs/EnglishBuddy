package com.morishjs.englishbuddy.data

import android.content.Context
import com.morishjs.englishbuddy.recoder.AudioRecorder
import javax.inject.Inject

class RecorderRepositoryImpl @Inject internal constructor(
    private val recorder: AudioRecorder,
): RecorderRepository {
    override fun startRecording(context: Context) =
        recorder.start(context)


    override fun stopRecording() = recorder.stop()

}