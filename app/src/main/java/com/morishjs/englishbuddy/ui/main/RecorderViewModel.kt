package com.morishjs.englishbuddy.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.morishjs.englishbuddy.recoder.AudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RecorderViewModel @Inject internal constructor(
    private val recorder: AudioRecorder
) : ViewModel() {
    private val _isStarted = MutableStateFlow(false)
    val isStarted: MutableStateFlow<Boolean> = _isStarted

    fun start(context: Context) {
        recorder.start(context)
        _isStarted.value = true
    }

    fun stop() {
        recorder.stop()
        _isStarted.value = false
    }
}