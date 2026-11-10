package com.example.nusalingo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = NusalingoColors.ActionPrimary,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = NusalingoColors.TextInverse,
            disabledContainerColor = containerColor.copy(alpha = if (isLoading) 1f else 0.5f),
            disabledContentColor = NusalingoColors.TextInverse
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = NusalingoColors.TextInverse,
                strokeWidth = 2.dp
            )
        } else {
            if (leadingIcon != null) {
                Icon(imageVector = leadingIcon, contentDescription = null)
                Spacer(Modifier.width(12.dp))
            }
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    borderColor: Color = NusalingoColors.ActionSecondary,
    contentColor: Color = NusalingoColors.ActionSecondary,
    height: Dp = 56.dp,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            if (enabled) borderColor else borderColor.copy(alpha = 0.4f)
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = NusalingoColors.Background,
            contentColor = contentColor,
            disabledContainerColor = NusalingoColors.Background,
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}