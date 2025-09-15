package com.example.rasago.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 简化版 Dark 颜色
private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF121212), // 深灰背景
    onBackground = Color.White      // 白色文字
)

// 简化版 Light 颜色
private val LightColorScheme = lightColorScheme(
    background = Color.White,        // 白色背景
    onBackground = Color.Black       // 黑色文字
)


@Composable
fun RasagoApp(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
