package com.lastwave.app.data.ai

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FreeGeminiProvider @Inject constructor(
    private val geminiByokProvider: GeminiByokProvider,
) {

    fun generateStream(
        modelName: String,
        systemPrompt: String,
        userPrompt: String,
        fallbackApiKey: String = "",
    ): Flow<AiResult> = flow {
        if (fallbackApiKey.isNotBlank()) {
            geminiByokProvider.generateStream(fallbackApiKey, modelName, systemPrompt, userPrompt).collect { emit(it) }
            return@flow
        }

        var didEmitSuccess = false
        runCatching {
            val generativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
                .generativeModel(modelName = modelName.ifBlank { "gemini-2.5-flash" })

            val fullPrompt = "$systemPrompt\n\nUser Question:\n$userPrompt"
            val responseFlow = generativeModel.generateContentStream(fullPrompt)
            val fullText = StringBuilder()

            responseFlow.collect { chunk ->
                val chunkText = chunk.text.orEmpty()
                if (chunkText.isNotEmpty()) {
                    fullText.append(chunkText)
                    didEmitSuccess = true
                    emit(AiResult.Streaming(chunk = chunkText, fullTextSoFar = fullText.toString()))
                }
            }

            if (fullText.isNotEmpty()) {
                emit(AiResult.Success(fullText.toString()))
            } else {
                emit(AiResult.Error("Default Firebase key expired. Please enter your free Gemini API key in Settings → AI to continue."))
            }
        }.getOrElse { e ->
            if (!didEmitSuccess && fallbackApiKey.isNotBlank()) {
                geminiByokProvider.generateStream(fallbackApiKey, modelName, systemPrompt, userPrompt).collect { emit(it) }
            } else {
                val msg = e.localizedMessage ?: "Free AI Provider Error"
                when {
                    msg.contains("API key expired", ignoreCase = true) ||
                    msg.contains("API_KEY_EXPIRED", ignoreCase = true) ||
                    msg.contains("AppCheck", ignoreCase = true) ||
                    msg.contains("expired", ignoreCase = true) -> {
                        emit(AiResult.Error("Default Firebase key expired. Please enter your free Gemini API key in Settings → AI to continue."))
                    }
                    msg.contains("429", ignoreCase = true) || msg.contains("quota", ignoreCase = true) -> emit(AiResult.RateLimited)
                    msg.contains("safety", ignoreCase = true) || msg.contains("blocked", ignoreCase = true) -> emit(AiResult.SafetyRefusal)
                    else -> emit(AiResult.Error("Default Firebase key expired. Please enter your free Gemini API key in Settings → AI to continue."))
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}
