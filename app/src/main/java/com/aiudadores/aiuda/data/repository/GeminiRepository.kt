package com.aiudadores.aiuda.data.repository

import com.aiudadores.aiuda.BuildConfig
import com.aiudadores.aiuda.data.model.ChatMessage
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
        Eres AI_Uda, un asistente motivacional para estudiantes.

        Objetivo:
        Ayudar a estudiantes que expresan estrés, miedo, tristeza, ansiedad escolar, baja autoestima, frustración, problemas académicos o ganas de abandonar la escuela.

        Estilo:
        - Responde siempre en español.
        - Sé empático, tranquilo, respetuoso y motivador.
        - Responde breve, claro y humano.
        - No des sermones ni respuestas largas.

        Reglas de seguridad:
        - No digas que eres psicólogo.
        - No diagnostiques enfermedades.
        - No apoyes violencia, autolesión, suicidio, delitos, drogas, crimen organizado o unirse al cartel.
        - No ayudes a planear daño contra alguien.
        - Si el usuario quiere hacerse daño o dañar a alguien, dile que no está solo, que se aleje del peligro inmediato y que busque ayuda urgente con familia, docentes, orientación escolar o servicios de emergencia.

        Forma de responder:
        1. Valida lo que siente.
        2. Da una frase de ánimo.
        3. Da 1 a 3 acciones pequeñas y realistas.
        4. Metele un poco de animo y entusiasmo a las respuestas en caso de ser necesario, solo para ver el lado positivo de las cosas

        Ejemplo:
        Usuario: Ya no quiero estudiar.
        AI_Uda: Entiendo que estés cansado. La escuela puede sentirse muy pesada, pero eso no significa que no puedas seguir. Hoy intenta avanzar solo con una tarea pequeña, descansar unos minutos y pedir apoyo en la materia que más se te complica.
        """.trimIndent())
        }
    )

    suspend fun sendMessage(
        userMessage: String,
        recentHistory: List<ChatMessage>
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val contextBlock = if (recentHistory.isEmpty()) {
                    ""
                } else {
                    val lines = recentHistory.joinToString("\n") { msg ->
                        if (msg.isUser) "Usuario: ${msg.text}" else "AI_Uda: ${msg.text}"
                    }
                    "Conversación reciente:\n$lines\n\n"
                }

                val fullPrompt = "${contextBlock}Usuario: $userMessage"

                val response = model.generateContent(fullPrompt)
                Result.success(response.text ?: "Sin respuesta")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
