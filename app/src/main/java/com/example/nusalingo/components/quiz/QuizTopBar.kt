package com.example.nusalingo.components.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun QuizTopBar(
    progress: Float,
    xp: Int,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "×",
            modifier = Modifier.clickable { onExit() },
            color = NusalingoColors.TextPrimary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Normal
        )

        val safeProgress = progress.coerceIn(0f, 1f)

        Box(
            modifier = Modifier
                .weight(1f)
                .height(12.dp)
                .clip(RoundedCornerShape(50))
                .background(NusalingoColors.InputBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(safeProgress)
                    .height(12.dp)
                    .clip(RoundedCornerShape(50))
                    .background(NusalingoColors.ActionPrimary)
            )
        }

        Text(
            text = "⚡ $xp",
            color = androidx.compose.ui.graphics.Color(0xFF36A853),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}