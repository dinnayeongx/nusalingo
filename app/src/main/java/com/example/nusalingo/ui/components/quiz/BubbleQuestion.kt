package com.example.nusalingo.ui.components.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.R
import com.example.nusalingo.ui.theme.JavaneseFont
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

enum class BubbleType {
    LATIN_TEXT,
    AKSARA_AUDIO,
    AKSARA_LARGE
}

private val BubbleBackground = Color(0xFFFFFBF2)
private val BubbleBorder = Color(0xFFD9A066)
private val LatinSpeechBubbleShape = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val tailWidth = with(density) { 12.dp.toPx() }
        val cornerRadius = with(density) { 8.dp.toPx() }
        val tailHalfHeight = with(density) { 7.dp.toPx() }
        val left = tailWidth
        val tailCenterY = cornerRadius + with(density) { 14.dp.toPx() }
        val path = Path().apply {
            moveTo(left + cornerRadius, 0f)
            lineTo(size.width - cornerRadius, 0f)
            quadraticTo(size.width, 0f, size.width, cornerRadius)
            lineTo(size.width, size.height - cornerRadius)
            quadraticTo(size.width, size.height, size.width - cornerRadius, size.height)
            lineTo(left + cornerRadius, size.height)
            quadraticTo(left, size.height, left, size.height - cornerRadius)
            lineTo(left, tailCenterY + tailHalfHeight)
            lineTo(0f, tailCenterY)
            lineTo(left, tailCenterY - tailHalfHeight)
            lineTo(left, cornerRadius)
            quadraticTo(left, 0f, left + cornerRadius, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun BubbleQuestion(
    text: String,
    type: BubbleType,
    modifier: Modifier = Modifier,
    onAudioClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(8.dp)

    when (type) {
        BubbleType.AKSARA_LARGE -> {
            Box(modifier = modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 36.dp)
                        .background(BubbleBackground, shape)
                        .border(1.dp, BubbleBorder, shape)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        fontFamily = JavaneseFont,
                        fontSize = 28.sp,
                        color = NusalingoColors.TextPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Image(
                    painter = painterResource(R.drawable.mascot_face),
                    contentDescription = null,
                    modifier = Modifier
                        .size(92.dp)
                        .align(Alignment.TopStart)
                )
            }
        }

        else -> {
            val isLatinText = type == BubbleType.LATIN_TEXT
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val mascotSize = when (type) {
                    BubbleType.LATIN_TEXT -> 104.dp
                    BubbleType.AKSARA_AUDIO -> 96.dp
                    BubbleType.AKSARA_LARGE -> 144.dp
                }

                Image(
                    painter = painterResource(R.drawable.mascot_face),
                    contentDescription = null,
                    modifier = Modifier.size(mascotSize)
                )

                Spacer(Modifier.width(if (isLatinText) 8.dp else 4.dp))

                if (isLatinText) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .defaultMinSize(minHeight = 76.dp)
                            .background(
                                BubbleBackground,
                                LatinSpeechBubbleShape
                            )
                            .border(
                                1.dp,
                                BubbleBorder,
                                LatinSpeechBubbleShape
                            )
                            .padding(start = 22.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            fontFamily = NunitoFont,
                            fontSize = 18.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NusalingoColors.TextPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .height(80.dp)
                            .background(BubbleBackground, shape)
                            .border(1.dp, BubbleBorder, shape)
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            onClick = onAudioClick,
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF3379DD)
                        ) {
                            Box(
                                modifier = Modifier.size(34.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🔊", fontSize = 18.sp)
                            }
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = text,
                            fontFamily = JavaneseFont,
                            fontSize = if (type == BubbleType.AKSARA_AUDIO) 24.sp else 32.sp,
                            color = NusalingoColors.TextPrimary,
                            textAlign = TextAlign.Left
                        )
                    }
                }
            }
        }
    }
}