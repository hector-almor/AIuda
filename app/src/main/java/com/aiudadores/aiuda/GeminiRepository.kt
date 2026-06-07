package com.aiudadores.aiuda

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiRepository {

    private val model = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 0.8f
            maxOutputTokens = 1024
        },
        systemInstruction = content {
            text("""
                Eres un consejero educativo empático y motivador.
                Tu rol es escuchar las preocupaciones de los estudiantes,
                validar sus emociones y brindar palabras de aliento genuinas.
                Responde en español, de forma cálida, breve (máximo 3 párrafos)
                y con un tono esperanzador. Nunca minimices sus problemas. 
                Desaconseja fuertemente cualquier acto que atente contra la integridad de cualquier persona.
                Desaconseja fuertemente cualquier acto ilícito.
            """.trimIndent())
        }
    )

    private val chat = model.startChat()

    suspend fun sendMessage(userMessage: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = chat.sendMessage(userMessage)
                Result.success(response.text ?: "Sin respuesta")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}