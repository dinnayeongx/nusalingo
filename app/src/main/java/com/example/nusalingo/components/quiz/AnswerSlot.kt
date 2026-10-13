package com.example.nusalingo.components.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun AnswerSlot(
    character: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    correct: Boolean? = null
) {
    val background = when (correct) {
        true -> Color(0xFFA8DDB0)
        false -> Color(0xFFF4B6B6)
        null -> if (character == null) Color(0xFFDADADA)
        else Color.White
    }

    Surface(
        modifier = modifier
            .defaultMinSize(minWidth = 38.dp, minHeight = 34.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(5.dp),
        color = background,
        border = BorderStroke(
            1.dp,
            if (correct == true) Color(0xFF36A853)
            else NusalingoColors.InputBorder
        )
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 4.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = character ?: "",
                color = NusalingoColors.TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}