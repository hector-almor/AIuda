package com.aiudadores.aiuda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aiudadores.aiuda.ui.theme.AI_UdaTheme
import com.miro8475.ai_uda.ui.screens.ChatScreen.PrincipalScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AI_UdaTheme {
                PrincipalScreen()
            }
        }
    }
}