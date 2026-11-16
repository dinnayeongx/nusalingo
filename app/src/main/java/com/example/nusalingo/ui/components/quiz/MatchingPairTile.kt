package com.example.nusalingo.ui.components.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.JavaneseFont
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun MatchingPairCard(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    matched: Boolean = false,
    matchedFlash: Boolean = false,
    isCharacter: Boolean = false,
    enabled: Boolean = true,
    incorrect: Boolean = false
) {
    val backgroundColor = when {
        matched && matchedFlash -> Color(0xFF69C681)
        matched -> NusalingoColors.GreenSecondary
        incorrect -> Color(0xFFF4B6B6)
        selected -> NusalingoColors.ActionPrimary
        else -> NusalingoColors.InputBackground
    }

    val textColor = when {
        incorrect -> NusalingoColors.TextPrimary
        selected && !matched -> NusalingoColors.TextInverse
        else -> NusalingoColors.TextPrimary
    }

    val borderColor = when {
        matched -> Color(0xFF53A96A)
        incorrect -> Color(0xFFD66A6A)
        selected -> NusalingoColors.ActionPrimary
        else -> NusalingoColors.InputBorder
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .alpha(if (matched && !matchedFlash) 0.48f else 1f)
            .clickable(enabled = enabled && !matched, onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = backgroundColor,
        border = BorderStroke(
            width = 2.dp,
            color = borderColor
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = textColor,
                fontFamily = if (isCharacter) JavaneseFont else NunitoFont,
                fontSize = if (isCharacter) 32.sp else 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}