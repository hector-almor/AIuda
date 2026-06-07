package com.aiudadores.aiuda.data.model

data class ChatSession(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "Nuevo Chat",
    val messages: List<ChatMessage> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val preview: String
        get() = messages.lastOrNull()?.text?.take(40)?.let {
            if (it.length == 40) "$it..." else it
        } ?: "No hay mensajes"
}