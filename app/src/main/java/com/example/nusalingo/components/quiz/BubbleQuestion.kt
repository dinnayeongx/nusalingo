package com.example.nusalingo.components.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.R
import com.example.nusalingo.ui.theme.NusalingoColors

enum class BubbleType {
    LATIN_TEXT,
    AKSARA_AUDIO,
    AKSARA_LARGE
}

private val BubbleBackground = Color(0xFFFFFBF2)
private val BubbleBorder = Color(0xFFD9A066)

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
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.mascot_face),
                    contentDescription = null,
                    modifier = Modifier.size(144.dp)
                )

                Spacer(Modifier.width(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(BubbleBackground, shape)
                        .border(1.dp, BubbleBorder, shape)
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (type == BubbleType.AKSARA_AUDIO) {
                        Surface(
                            onClick = onAudioClick
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🔊", fontSize = 20.sp)
                            }
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = text,
                            fontSize = 32.sp,
                            color = NusalingoColors.TextPrimary,
                            textAlign = TextAlign.Left
                        )
                    } else {
                        Text(
                            text = text,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = NusalingoColors.TextPrimary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}