package com.example.nusalingo.feature.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nusalingo.ui.theme.NunitoFont
import com.example.nusalingo.ui.theme.NusalingoColors
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nusalingo.feature.home.ui.AksaraItem
import com.example.nusalingo.feature.home.ui.HomeState
import com.example.nusalingo.feature.home.ui.HomeViewModel

@Composable
fun PilihAksaraRoute(
    onBack: () -> Unit,
    onSelect: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    PilihAksaraScreen(
        state = state,
        onBack = onBack,
        onSelectAksara = { item ->
            if (item.id == "jawa") {
                viewModel.selectAksara(item)
                onSelect()
            }
        }
    )
}

@Composable
fun PilihAksaraScreen(
    state: HomeState,
    onBack: () -> Unit,
    onSelectAksara: (AksaraItem) -> Unit
) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 20.dp)) {
            Text("‹", modifier = Modifier.clickable(onClick = onBack).padding(end = 16.dp), fontSize = 42.sp, color = Color.Black)
            Text("Pilih Aksara Daerah", fontFamily = NunitoFont, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = NusalingoColors.TextPrimary)
        }

        state.aksaraList.forEach { item ->
            val isEnabled = item.name.equals("Aksara Jawa", ignoreCase = true)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 28.dp,
                            topEnd = 28.dp
                        )
                    )
                    .background(NusalingoColors.CardCream)
                    .clickable(enabled = isEnabled) {
                        if (isEnabled) {
                            onSelectAksara(item)
                        }
                    }
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(112.dp)
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        item.name,
                        fontFamily = NunitoFont,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEnabled) {
                            NusalingoColors.ActionPrimary
                        } else {
                            NusalingoColors.TextSecondary.copy(alpha = 0.5f)
                        }
                    )
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            if (isEnabled) "Tersedia" else "Segera Hadir",
                            modifier = Modifier.weight(1f),
                            fontFamily = NunitoFont,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isEnabled) {
                                Color.Black
                            } else {
                                NusalingoColors.TextSecondary
                            }
                        )

                        Text(
                            if (isEnabled) item.progressText else "0 to 0",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = NusalingoColors.TextSecondary
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(18.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isEnabled) {
                                    NusalingoColors.BrownAccent
                                } else {
                                    NusalingoColors.Disabled
                                }
                            )
                    )
                }
            }
        }
    }
}
