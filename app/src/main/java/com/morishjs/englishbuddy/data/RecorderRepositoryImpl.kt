package com.morishjs.englishbuddy.data

import android.content.Context
import com.morishjs.englishbuddy.recoder.AudioRecorder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RecorderRepositoryImpl @Inject internal constructor(
    private val recorder: AudioRecorder,
    @ApplicationContext private val context: Context
): RecorderRepository {
    override fun startRecording() =
        recorder.start(context)


    override fun stopRecording() = recorder.stop()

}