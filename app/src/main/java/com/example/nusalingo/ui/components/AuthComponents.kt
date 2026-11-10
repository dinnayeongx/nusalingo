package com.example.nusalingo.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun AuthScaffold(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBack: (() -> Unit)? = null,
    bottomBar: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 24.dp),
    ) {
        if (title != null && onBack != null) {
            AuthTopBar(title = title, onBack = onBack)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            content = content
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = bottomBar
        )
    }
}

@Composable
fun AuthTopBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(NusalingoColors.ActionPrimary)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Kembali",
                tint = Color.White
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = NusalingoColors.TextPrimary
        )
    }
}

@Composable
fun AuthMascot(@DrawableRes res: Int, modifier: Modifier = Modifier, size: Dp = 180.dp) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = res),
            contentDescription = "Maskot NusaLingo",
            modifier = Modifier.size(size),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun AuthHeading(title: String, subtitle: String, centered: Boolean = true) {
    val align = if (centered) TextAlign.Center else TextAlign.Start
    Text(
        text = title,
        modifier = Modifier.fillMaxWidth(),
        textAlign = align,
        fontSize = 28.sp,
        fontWeight = FontWeight.ExtraBold,
        color = NusalingoColors.TextPrimary
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = subtitle,
        modifier = Modifier.fillMaxWidth(),
        textAlign = align,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.Medium,
        color = NusalingoColors.TextSecondary
    )
}

@Composable
fun FieldLabel(text: String, color: Color = NusalingoColors.TextPrimary) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        color = color
    )
    Spacer(Modifier.height(6.dp))
}

@Composable
fun AuthInfoBanner(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NusalingoColors.ActionSecondary.copy(alpha = 0.15f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = NusalingoColors.ActionSecondary)
        Spacer(Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            color = NusalingoColors.TextPrimary
        )
    }
}

val ArrowForwardIcon: ImageVector get() = Icons.AutoMirrored.Outlined.ArrowForward