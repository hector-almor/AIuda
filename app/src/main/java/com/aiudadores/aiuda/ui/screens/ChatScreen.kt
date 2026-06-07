package com.aiudadores.aiuda.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiudadores.aiuda.ui.components.ChatBubble
import com.aiudadores.aiuda.ui.components.ChatInputBar
import com.aiudadores.aiuda.ui.theme.AzulPrimario
import com.aiudadores.aiuda.ui.theme.FondoPrincipal
import com.aiudadores.aiuda.ui.theme.RojoError
import com.aiudadores.aiuda.ui.theme.TextoPrincipal
import com.aiudadores.aiuda.ui.theme.TextoSecundario
import com.aiudadores.aiuda.ui.viewmodel.ChatViewModel
import com.aiudadores.aiuda.ui.viewmodel.ChatViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun PrincipalScreen() {
    val context = LocalContext.current
    val viewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(context))

    var mensaje by remember { mutableStateOf("") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.currentMessages.size) {
        if (uiState.currentMessages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.currentMessages.size - 1)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ChatHistoryDrawer(
                sessions = uiState.sessions,
                currentSessionId = uiState.currentSession?.id,
                onNewChat = {
                    viewModel.createNewSession()
                    scope.launch { drawerState.close() }
                },
                onSelectSession = { session ->
                    viewModel.selectSession(session)
                    scope.launch { drawerState.close() }
                },
                onDeleteSession = { sessionId ->
                    viewModel.deleteSession(sessionId)
                },
                onRenameSession = { sessionId, newName ->
                    viewModel.renameSession(sessionId, newName)
                }
            )
        }
    ) {
        Scaffold { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(FondoPrincipal)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Abrir historial",
                            tint = TextoPrincipal
                        )
                    }

                    Spacer(modifier = androidx.compose.ui.Modifier.size(8.dp))

                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = uiState.currentSession?.name ?: "AI_Uda",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = TextoPrincipal
                        )
                        Text(
                            text = "Siempre listo para AIudarte!",
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = TextoSecundario
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:911")
                        }
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Llamar",
                            tint = AzulPrimario
                        )
                    }
                }

                HorizontalDivider()

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.currentMessages) { msg ->
                        ChatBubble(texto = msg.text, esUsuario = msg.isUser)
                    }

                    if (uiState.isLoading) {
                        item {
                            Row(modifier = Modifier.padding(8.dp)) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = AzulPrimario
                                )
                            }
                        }
                    }

                    uiState.error?.let { error ->
                        item {
                            Text(
                                text = error,
                                color = RojoError,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                ChatInputBar(
                    value = mensaje,
                    onValueChange = { mensaje = it },
                    onSend = {
                        viewModel.sendMessage(mensaje)
                        mensaje = ""
                    }
                )
            }
        }
    }
}