package com.morishjs.englishbuddy.domain

import android.media.MediaRecorder

data class Recorder(val mediaRecorder: MediaRecorder) {
    fun maxAmplitude() = mediaRecorder.maxAmplitude
}