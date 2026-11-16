package com.example.nusalingo.ui.components.quiz

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import com.example.nusalingo.ui.theme.NusalingoColors
import kotlin.random.Random

private data class AnswerConfettiPiece(
    val x: Float,
    val drift: Float,
    val depth: Float,
    val rotation: Float,
    val width: Float,
    val height: Float,
    val color: Color
)

@Composable
fun QuizAnswerConfetti(key: Int, modifier: Modifier = Modifier) {
    val density = LocalDensity.current.density
    val progress = remember(key) { Animatable(0f) }
    val colors = listOf(
        Color(0xFF16AD61),
        NusalingoColors.ActionPrimary,
        Color(0xFFFFC857),
        Color(0xFFE8795A),
        Color(0xFF8A71C8)
    )
    val pieces = remember(key, density) {
        val random = Random(key)
        List(32) {
            AnswerConfettiPiece(
                x = 0.12f + random.nextFloat() * 0.76f,
                drift = (random.nextFloat() - 0.5f) * 70f * density,
                depth = 0.35f + random.nextFloat() * 0.65f,
                rotation = random.nextFloat() * 360f,
                width = (4f + random.nextFloat() * 4f) * density,
                height = (5f + random.nextFloat() * 5f) * density,
                color = colors[random.nextInt(colors.size)]
            )
        }
    }

    LaunchedEffect(key) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(900)
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        pieces.forEach { piece ->
            val x = (size.width * piece.x + piece.drift * progress.value)
                .coerceIn(0f, size.width)
            val y = size.height * progress.value * piece.depth
            val alpha = (1f - progress.value).coerceIn(0f, 1f)

            rotate(
                degrees = piece.rotation + 360f * progress.value,
                pivot = Offset(x, y)
            ) {
                drawRect(
                    color = piece.color.copy(alpha = alpha),
                    topLeft = Offset(x, y),
                    size = Size(piece.width, piece.height)
                )
            }
        }
    }
}
