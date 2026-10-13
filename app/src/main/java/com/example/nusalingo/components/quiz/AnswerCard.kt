package com.example.nusalingo.components.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun AnswerCard(
    text: String,
    isAksara: Boolean,
    used: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFFFE0A8),
        modifier = modifier
            .alpha(if (used) 0.35f else 1f)
            .defaultMinSize(minWidth = 80.dp, minHeight = 50.dp)
            .clickable(enabled = enabled && !used, onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                fontSize = if (isAksara) 28.sp else 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NusalingoColors.TextPrimary
            )
        }
    }
}

@Composable
private fun AnswerChip(
    text: String,
    isAksara: Boolean,
    state: QuizAnswerState,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val (bg, border) = when (state) {
        QuizAnswerState.CORRECT -> Color(0xFFA8D8A0) to Color(0xFF5DB075)
        QuizAnswerState.WRONG -> Color(0xFFF4B6B6) to Color(0xFFD66A6A)
        else -> Color.White to Color(0xFFE6DCCB)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bg,
        border = BorderStroke(1.dp, border),
        modifier = Modifier
            .defaultMinSize(minWidth = 80.dp, minHeight = 50.dp)
            .clickable(enabled = enabled, onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                fontSize = if (isAksara) 28.sp else 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NusalingoColors.TextPrimary
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AnswerSlotRow(
    tokens: List<String>,
    totalSlots: Int,
    isAksara: Boolean,
    state: QuizAnswerState,
    enabled: Boolean,
    onTokenClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minWidth = 80.dp, minHeight = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            tokens.forEachIndexed { index, token ->
                AnswerChip(
                    text = token,
                    isAksara = isAksara,
                    state = state,
                    enabled = enabled,
                    onClick = { onTokenClick(index) }
                )
            }

            repeat((totalSlots - tokens.size).coerceAtLeast(0)) {
                Box(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 80.dp, minHeight = 50.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.5.dp)
                .background(Color(0xFFE8C9A0))
        )
    }
}