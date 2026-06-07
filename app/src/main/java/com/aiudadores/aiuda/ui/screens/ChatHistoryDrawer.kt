package com.aiudadores.aiuda.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiudadores.aiuda.data.model.ChatSession
import com.aiudadores.aiuda.ui.components.ChatSessionItem
import com.aiudadores.aiuda.ui.theme.AzulPrimario
import com.aiudadores.aiuda.ui.theme.FondoBlanco
import com.aiudadores.aiuda.ui.theme.TextoPrincipal

@Composable
fun ChatHistoryDrawer(
    sessions: List<ChatSession>,
    currentSessionId: String?,
    onNewChat: () -> Unit,
    onSelectSession: (ChatSession) -> Unit,
    onDeleteSession: (String) -> Unit,
    onRenameSession: (String, String) -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis Chats",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextoPrincipal,
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = onNewChat,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulPrimario,
                    contentColor = FondoBlanco,
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nuevo chat",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        HorizontalDivider()

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(sessions, key = { it.id }) { session ->
                ChatSessionItem(
                    session = session,
                    isSelected = session.id == currentSessionId,
                    onClick = { onSelectSession(session) },
                    onDelete = { onDeleteSession(session.id) },
                    onRename = { newName -> onRenameSession(session.id, newName) }
                )
            }
        }
    }
}