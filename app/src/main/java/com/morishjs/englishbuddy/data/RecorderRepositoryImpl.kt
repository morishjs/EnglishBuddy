package com.morishjs.englishbuddy.data

import com.morishjs.englishbuddy.annotations.RecorderVoskClient
import com.morishjs.englishbuddy.recorder.AudioRecorder
import javax.inject.Inject

class RecorderRepositoryImpl @Inject internal constructor(
    @RecorderVoskClient private val recorder: AudioRecorder,
) : RecorderRepository {
    override val transcription = recorder.transcription

    override fun startRecording() =
        recorder.startRecording()

    override fun stopRecording() = recorder.stopRecording()

}