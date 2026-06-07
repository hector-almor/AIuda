package com.aiudadores.aiuda.data.repository

import android.content.Context
import com.aiudadores.aiuda.data.model.ChatMessage
import com.aiudadores.aiuda.data.model.ChatSession
import org.json.JSONArray
import org.json.JSONObject

class ChatHistoryRepository(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getSessions(): List<ChatSession> {
        val json = prefs.getString(KEY_SESSIONS, null) ?: return emptyList()
        return try {
            val array = JSONArray(json)
            (0 until array.length()).map { i -> sessionFromJson(array.getJSONObject(i)) }
                .sortedByDescending { it.updatedAt }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun saveSession(session: ChatSession) {
        val sessions = getSessions().toMutableList()
        val index = sessions.indexOfFirst { it.id == session.id }
        if (index >= 0) {
            sessions[index] = session
        } else {
            sessions.add(0, session)
        }
        persistSessions(sessions)
    }

    fun deleteSession(sessionId: String) {
        val sessions = getSessions().filter { it.id != sessionId }
        persistSessions(sessions)
    }

    fun renameSession(sessionId: String, newName: String) {
        val sessions = getSessions().toMutableList()
        val index = sessions.indexOfFirst { it.id == sessionId }
        if (index >= 0) {
            sessions[index] = sessions[index].copy(name = newName)
            persistSessions(sessions)
        }
    }

    private fun persistSessions(sessions: List<ChatSession>) {
        val array = JSONArray()
        sessions.forEach { array.put(sessionToJson(it)) }
        prefs.edit().putString(KEY_SESSIONS, array.toString()).apply()
    }

    private fun sessionToJson(session: ChatSession): JSONObject {
        return JSONObject().apply {
            put("id", session.id)
            put("name", session.name)
            put("createdAt", session.createdAt)
            put("updatedAt", session.updatedAt)
            val messagesArray = JSONArray()
            session.messages.forEach { msg ->
                messagesArray.put(JSONObject().apply {
                    put("id", msg.id)
                    put("text", msg.text)
                    put("isUser", msg.isUser)
                    put("timestamp", msg.timestamp)
                })
            }
            put("messages", messagesArray)
        }
    }

    private fun sessionFromJson(json: JSONObject): ChatSession {
        val messagesArray = json.getJSONArray("messages")
        val messages = (0 until messagesArray.length()).map { i ->
            val m = messagesArray.getJSONObject(i)
            ChatMessage(
                id = m.getString("id"),
                text = m.getString("text"),
                isUser = m.getBoolean("isUser"),
                timestamp = m.getLong("timestamp")
            )
        }
        return ChatSession(
            id = json.getString("id"),
            name = json.getString("name"),
            messages = messages,
            createdAt = json.getLong("createdAt"),
            updatedAt = json.getLong("updatedAt")
        )
    }

    companion object {
        private const val PREFS_NAME = "aiuda_chat_history"
        private const val KEY_SESSIONS = "sessions"
    }
}