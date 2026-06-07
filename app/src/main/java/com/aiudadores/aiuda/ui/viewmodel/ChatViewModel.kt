package com.aiudadores.aiuda.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiudadores.aiuda.data.model.ChatMessage
import com.aiudadores.aiuda.data.model.ChatSession
import com.aiudadores.aiuda.data.repository.ChatHistoryRepository
import com.aiudadores.aiuda.data.repository.GeminiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel(
    private val geminiRepository: GeminiRepository,
    private val historyRepository: ChatHistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    init {
        loadSessions()
    }

    // ─── Session management ───────────────────────────────────────────────────

    private fun loadSessions() {
        val sessions = historyRepository.getSessions()
        val current = sessions.firstOrNull() ?: createNewSessionObject()
        _uiState.update { it.copy(sessions = sessions, currentSession = current) }
        if (sessions.isEmpty()) historyRepository.saveSession(current)
    }

    fun createNewSession() {
        val newSession = createNewSessionObject()
        historyRepository.saveSession(newSession)
        val sessions = historyRepository.getSessions()
        _uiState.update { it.copy(sessions = sessions, currentSession = newSession) }
    }

    fun selectSession(session: ChatSession) {
        _uiState.update { it.copy(currentSession = session, error = null) }
    }

    fun deleteSession(sessionId: String) {
        historyRepository.deleteSession(sessionId)
        val sessions = historyRepository.getSessions()
        val current = if (_uiState.value.currentSession?.id == sessionId) {
            sessions.firstOrNull() ?: createNewSessionObject().also { historyRepository.saveSession(it) }
        } else {
            _uiState.value.currentSession
        }
        val updatedSessions = historyRepository.getSessions()
        _uiState.update { it.copy(sessions = updatedSessions, currentSession = current) }
    }

    fun renameSession(sessionId: String, newName: String) {
        historyRepository.renameSession(sessionId, newName)
        val sessions = historyRepository.getSessions()
        val current = if (_uiState.value.currentSession?.id == sessionId) {
            sessions.find { it.id == sessionId }
        } else {
            _uiState.value.currentSession
        }
        _uiState.update { it.copy(sessions = sessions, currentSession = current) }
    }

    // ─── Messaging ────────────────────────────────────────────────────────────

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val session = _uiState.value.currentSession ?: return

        // Últimos 4 mensajes como contexto
        val recentHistory = session.messages.takeLast(2)

        val userMessage = ChatMessage(text = userText, isUser = true)
        val updatedSession = session.copy(
            messages = session.messages + userMessage,
            updatedAt = System.currentTimeMillis(),
            name = if (session.messages.isEmpty()) generateSessionName(userText) else session.name
        )

        updateAndPersistSession(updatedSession)
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            geminiRepository.sendMessage(userText, recentHistory).fold(
                onSuccess = { responseText ->
                    val aiMessage = ChatMessage(text = responseText, isUser = false)
                    val withResponse = updatedSession.copy(
                        messages = updatedSession.messages + aiMessage,
                        updatedAt = System.currentTimeMillis()
                    )
                    updateAndPersistSession(withResponse)
                    _uiState.update { it.copy(isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private fun updateAndPersistSession(session: ChatSession) {
        historyRepository.saveSession(session)
        val sessions = historyRepository.getSessions()
        _uiState.update { it.copy(sessions = sessions, currentSession = session) }
    }

    private fun createNewSessionObject(): ChatSession = ChatSession(name = "Nuevo Chat")

    private fun generateSessionName(firstMessage: String): String {
        return firstMessage.take(30).let { if (firstMessage.length > 30) "$it..." else it }
    }
}