package com.example.nusalingo.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.R
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun BrandLogo(modifier: Modifier = Modifier) {
    Row(modifier.padding(start = 8.dp, top = 12.dp, bottom = 24.dp)) {
        Text("Nusa", fontFamily = NunitoFont, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = NusalingoColors.LogoNusa)
        Text("Lingo", fontFamily = NunitoFont, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = NusalingoColors.LogoLingo)
    }
}

@Composable
fun HomeScreen(onExplore: () -> Unit, onLevel: (Int) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
        BrandLogo()
        Row(
            Modifier.fillMaxWidth().height(70.dp).clip(RoundedCornerShape(18.dp))
                .background(NusalingoColors.CardCream).clickable(onClick = onExplore).padding(horizontal = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Jelajahi Aksara Lainnya", Modifier.weight(1f), fontFamily = NunitoFont,
                fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NusalingoColors.ActionPrimary)
            Icon(Icons.Outlined.ChevronRight, contentDescription = "Jelajahi", tint = NusalingoColors.ActionPrimary, modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.height(16.dp))
        LearningLevelCard("Mengenal Aksara", 1) { onLevel(1) }
        Spacer(Modifier.height(16.dp))
        LearningLevelCard("Menggabungkan Aksara", 2) { onLevel(2) }
        Spacer(Modifier.height(16.dp))
        LearningLevelCard("Membangun Kalimat", 3) { onLevel(3) }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
fun LearningLevelCard(title: String, level: Int, onClick: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().height(174.dp).clip(RoundedCornerShape(28.dp))
            .background(NusalingoColors.CardYellow).clickable(onClick = onClick)
    ) {
        Box(Modifier.fillMaxWidth().weight(1f).background(NusalingoColors.ActionPrimary)) {
            Text(title, Modifier.align(Alignment.TopStart).padding(start = 24.dp, top = 24.dp, end = 112.dp),
                fontFamily = NunitoFont, fontWeight = FontWeight.Bold, fontSize = 23.sp,
                lineHeight = 29.sp, color = Color.White, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = Color.White,
                modifier = Modifier.align(Alignment.TopEnd).padding(top = 18.dp, end = 14.dp).size(30.dp))
            Image(
                painter = painterResource(R.drawable.mascot_jump),
                contentDescription = "Maskot NusaLingo",
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 8.dp).size(132.dp)
            )
        }
        Box(Modifier.fillMaxWidth().height(54.dp).padding(start = 24.dp), contentAlignment = Alignment.CenterStart) {
            Text("Level $level", fontFamily = NunitoFont, fontSize = 22.sp,
                fontWeight = FontWeight.Bold, color = NusalingoColors.ActionPrimary)
        }
    }
}
