package com.aiudadores.aiuda.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val EsquemaColores = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = FondoBlanco,
    primaryContainer = AzulPrimarioClaro,
    onPrimaryContainer = TextoPrincipal,

    secondary = VerdeAcento,
    onSecondary = FondoBlanco,
    secondaryContainer = VerdeAcentoClaro,
    onSecondaryContainer = TextoPrincipal,

    background = FondoPrincipal,
    onBackground = TextoPrincipal,

    surface = FondoBlanco,
    onSurface = TextoPrincipal,
    surfaceVariant = FondoTarjeta,
    onSurfaceVariant = TextoSecundario,

    error = RojoError,
    onError = FondoBlanco
)

@Composable
fun AI_UdaTheme(
    content: @Composable () -> Unit
) {
    val vista = LocalView.current

    if (!vista.isInEditMode) {
        SideEffect {
            val ventana = (vista.context as Activity).window
            ventana.statusBarColor = AzulPrimario.toArgb()
            WindowCompat.getInsetsController(
                ventana,
                vista
            ).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = EsquemaColores,
        typography = Typography,
        content = content
    )
}