package com.morishjs.englishbuddy.recorder

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RecorderImpl @Inject internal constructor(
    private val recorder: AudioRecorder,
    @ApplicationContext private val context: Context
) : Recorder {
    override fun startRecording() =
        recorder.start(context)

    override fun stopRecording() = recorder.stop()

}