package com.example.nusalingo.components.quiz

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun CharacterTile(
    character: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false
) {
    Surface(
        modifier = modifier
            .defaultMinSize(minWidth = 42.dp, minHeight = 36.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(7.dp),
        color = if (selected) NusalingoColors.InputBorder
        else if (enabled) androidx.compose.ui.graphics.Color(0xFFFFDEA0)
        else NusalingoColors.InputBackground
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = character,
                color = NusalingoColors.TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}