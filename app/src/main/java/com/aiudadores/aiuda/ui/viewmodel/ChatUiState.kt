package com.aiudadores.aiuda.ui.viewmodel

import com.aiudadores.aiuda.data.model.ChatMessage
import com.aiudadores.aiuda.data.model.ChatSession

data class ChatUiState(
    val sessions: List<ChatSession> = emptyList(),
    val currentSession: ChatSession? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val currentMessages: List<ChatMessage>
        get() = currentSession?.messages ?: emptyList()
}