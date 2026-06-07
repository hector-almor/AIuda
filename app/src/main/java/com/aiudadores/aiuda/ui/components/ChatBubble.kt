package com.aiudadores.aiuda.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiudadores.aiuda.ui.theme.BurbujaIA
import com.aiudadores.aiuda.ui.theme.BurbujaIATexto
import com.aiudadores.aiuda.ui.theme.BurbujaUsuario
import com.aiudadores.aiuda.ui.theme.BurbujaUsuarioTexto

@Composable
fun ChatBubble(
    texto: String,
    esUsuario: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (esUsuario) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (esUsuario) 18.dp else 4.dp,
                        bottomEnd = if (esUsuario) 4.dp else 18.dp
                    )
                )
                .background(
                    color = if (esUsuario) BurbujaUsuario else BurbujaIA,
                    shape = RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (esUsuario) 18.dp else 4.dp,
                        bottomEnd = if (esUsuario) 4.dp else 18.dp
                    )
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = texto,
                color = if (esUsuario) BurbujaUsuarioTexto else BurbujaIATexto,
                fontSize = 15.sp
            )
        }
    }
}