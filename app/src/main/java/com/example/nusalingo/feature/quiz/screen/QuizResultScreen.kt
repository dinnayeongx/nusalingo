package com.example.nusalingo.feature.quiz.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.R
import com.example.nusalingo.ui.components.PrimaryButton
import com.example.nusalingo.ui.components.SecondaryButton
import com.example.nusalingo.ui.theme.NusalingoColors
import kotlin.random.Random

private const val CELEBRATION_DURATION_MS = 3200

private data class CelebrationPiece(
    val x: Float,
    val drift: Float,
    val depth: Float,
    val rotation: Float,
    val width: Float,
    val height: Float,
    val color: Color
)

@Composable
fun QuizResultScreen(
    xpEarned: Int,
    correctAnswers: Int,
    totalQuestions: Int,
    onContinue: () -> Unit,
    onHome: () -> Unit,
    continueText: String = "Lanjutkan Belajar",
    showStats: Boolean = true,
    totalXp: Int? = null,
    secondaryActionText: String? = null,
    continueEnabled: Boolean = true
) {
    val accuracy = if (totalQuestions > 0) {
        correctAnswers * 100 / totalQuestions
    } else {
        0
    }

    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }

    val mascotScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.72f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "mascotScale"
    )
    val animatedXp by animateIntAsState(
        targetValue = xpEarned,
        animationSpec = tween(1100),
        label = "earnedXp"
    )
    val animatedTotalXp by animateIntAsState(
        targetValue = totalXp ?: 0,
        animationSpec = tween(1500),
        label = "totalXp"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.mascot_jump),
                contentDescription = "Maskot merayakan keberhasilan",
                modifier = Modifier
                    .size(240.dp)
                    .scale(mascotScale),
                contentScale = ContentScale.Fit
            )

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Misi Selesai!",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NusalingoColors.TextPrimary
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Hebat! Kamu berhasil menuntaskan tantangan aksara ini.",
                fontSize = 15.sp,
                color = NusalingoColors.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = NusalingoColors.ActionPrimary
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "XP DARI LATIHAN INI",
                        color = NusalingoColors.TextInverse.copy(alpha = 0.78f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⚡",
                            color = Color(0xFFFFD56A),
                            fontSize = 30.sp
                        )
                        Spacer(Modifier.size(8.dp))
                        Text(
                            text = "+$animatedXp XP",
                            color = NusalingoColors.TextInverse,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.White.copy(alpha = 0.24f))
                    )
                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total perolehan XP saat ini",
                            color = NusalingoColors.TextInverse,
                            fontSize = 13.sp
                        )
                        Text(
                            text = totalXp?.let { "$animatedTotalXp XP" } ?: "Menyimpan...",
                            color = Color(0xFFFFD56A),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            if (showStats) {
                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ResultStat(
                        value = "$correctAnswers / $totalQuestions",
                        label = "Jawaban benar",
                        modifier = Modifier.weight(1f)
                    )

                    ResultStat(
                        value = "$accuracy%",
                        label = "Nilai kuis",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            PrimaryButton(
                text = continueText,
                onClick = onContinue,
                enabled = continueEnabled
            )
            if (secondaryActionText != null) {
                Spacer(Modifier.height(12.dp))
                SecondaryButton(
                    text = secondaryActionText,
                    onClick = onHome
                )
            }
        }

        CelebrationConfetti(key = 1)
    }
}

@Composable
private fun CelebrationConfetti(key: Int) {
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
        List(120) {
            CelebrationPiece(
                x = random.nextFloat(),
                drift = (random.nextFloat() - 0.5f) * 180f * density,
                depth = 0.25f + random.nextFloat() * 0.75f,
                rotation = random.nextFloat() * 360f,
                width = (7f + random.nextFloat() * 6f) * density,
                height = (9f + random.nextFloat() * 8f) * density,
                color = colors[random.nextInt(colors.size)]
            )
        }
    }

    LaunchedEffect(key) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(CELEBRATION_DURATION_MS)
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        pieces.forEach { piece ->
            val x = (size.width * piece.x + piece.drift * progress.value)
                .coerceIn(0f, size.width)
            val y = -piece.height + size.height * progress.value * piece.depth
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

@Composable
private fun ResultStat(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = NusalingoColors.SurfacePrimary,
        border = BorderStroke(width = 1.dp, color = NusalingoColors.InputBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = NusalingoColors.TextPrimary
            )

            Text(
                text = label,
                fontSize = 13.sp,
                color = NusalingoColors.TextSecondary
            )
        }
    }
}
