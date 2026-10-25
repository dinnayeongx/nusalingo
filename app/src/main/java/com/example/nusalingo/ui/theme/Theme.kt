
package com.example.nusalingo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NusalingoLightColorScheme = lightColorScheme(
    primary = Color(0xFF1B365D),
    onPrimary = Color(0xFFFFFFFF),

    secondary = Color(0xFF8B5A2B),
    onSecondary = Color(0xFFFFFFFF),

    tertiary = Color(0xFFC86D51),
    onTertiary = Color(0xFFFFFFFF),

    background = Color(0xFFF9F6F0),
    onBackground = Color(0xFF2C2C2C),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF2C2C2C),

    surfaceVariant = Color(0xFFFFFCF6),
    onSurfaceVariant = Color(0xFF706C7E),

    outline = Color(0xFFE6DDCD)
)

private val NusalingoDarkColorScheme = darkColorScheme(
    primary = Color(0xFF9DB7E0),
    onPrimary = Color(0xFF102747),

    secondary = Color(0xFFE0B38B),
    onSecondary = Color(0xFF39220F),

    tertiary = Color(0xFFFFB49D),
    onTertiary = Color(0xFF512012),

    background = Color(0xFF191D24),
    onBackground = Color(0xFFF9F6F0),

    surface = Color(0xFF222833),
    onSurface = Color(0xFFF9F6F0),

    surfaceVariant = Color(0xFF303640),
    onSurfaceVariant = Color(0xFFD0C8BC),

    outline = Color(0xFF706C7E)
)

@Composable
fun NusalingoTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        NusalingoDarkColorScheme
    } else {
        NusalingoLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}