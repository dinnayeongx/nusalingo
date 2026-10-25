package com.example.nusalingo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun SelectionItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showCheckbox: Boolean = false
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 42.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = NusalingoColors.InputBackground,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected)
                NusalingoColors.ActionPrimary
            else
                NusalingoColors.InputBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                color = NusalingoColors.TextPrimary,
                fontSize = 13.sp,
                fontWeight = if (selected)
                    FontWeight.SemiBold
                else
                    FontWeight.Normal,
                textAlign = if (showCheckbox)
                    TextAlign.Start
                else
                    TextAlign.Center
            )

            if (showCheckbox) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = { onClick() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = NusalingoColors.ActionPrimary,
                        uncheckedColor = NusalingoColors.InputBorder
                    )
                )
            }
        }
    }
}