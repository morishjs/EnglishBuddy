package com.morishjs.englishbuddy.speech_to_text

import com.aallam.openai.api.audio.TranscriptionRequest
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import okio.source
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.name

class SpeechToTextImpl @Inject constructor(
    private val openAI: OpenAI
) : SpeechToText {
    override suspend fun transcript(path: Path): String {
        val request = TranscriptionRequest(
            audio = FileSource(
                name = path.name,
                source = path.source(),
            ),
            model = ModelId("whisper-1"),
            language = "en",
        )

        val response = openAI.transcription(request)
        return response.text
    }
}