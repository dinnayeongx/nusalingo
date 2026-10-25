package com.example.nusalingo.screens.home

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

@Composable
fun PilihAksaraScreen(onBack: () -> Unit, onSelect: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 20.dp)) {
            Text("‹", modifier = Modifier.clickable(onClick = onBack).padding(end = 16.dp), fontSize = 42.sp, color = Color.Black)
            Text("Pilih Aksara Daerah", fontFamily = NunitoFont, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = NusalingoColors.TextPrimary)
        }
        listOf("Aksara Jawa" to true, "Aksara Madura" to false, "Aksara Bali" to false).forEach { (name, available) ->
            Column(Modifier.fillMaxWidth().padding(bottom = 24.dp).clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)).background(NusalingoColors.CardCream)) {
                Box(Modifier.fillMaxWidth().height(112.dp).padding(horizontal = 24.dp), contentAlignment = Alignment.CenterStart) {
                    Text(name, fontFamily = NunitoFont, fontSize = 27.sp, fontWeight = FontWeight.Bold, color = NusalingoColors.ActionPrimary)
                }
                Column(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 24.dp, vertical = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (available) "Tersedia" else "Terkunci", modifier = Modifier.weight(1f),
                            fontFamily = NunitoFont, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(if (available) "3 to 3" else "0 to 0", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Box(Modifier.fillMaxWidth().height(18.dp).clip(RoundedCornerShape(20.dp))
                        .background(if (available) NusalingoColors.BrownAccent else NusalingoColors.Disabled))
                }
            }
        }
    }
}

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
            viewModel.selectAksara(item) // Memanggil fungsi di ViewModel
            onSelect()
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

        // Menggunakan data aksaraList dari ViewModel
        state.aksaraList.forEach { item ->
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(NusalingoColors.CardCream)
                    .clickable(enabled = item.isAvailable) { onSelectAksara(item) }
            ) {
                Box(Modifier.fillMaxWidth().height(112.dp).padding(horizontal = 24.dp), contentAlignment = Alignment.CenterStart) {
                    Text(item.name, fontFamily = NunitoFont, fontSize = 27.sp, fontWeight = FontWeight.Bold, color = NusalingoColors.ActionPrimary)
                }
                Column(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 24.dp, vertical = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (item.isAvailable) "Tersedia" else "Terkunci", modifier = Modifier.weight(1f),
                            fontFamily = NunitoFont, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text(item.progressText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Box(Modifier.fillMaxWidth().height(18.dp).clip(RoundedCornerShape(20.dp))
                        .background(if (item.isAvailable) NusalingoColors.BrownAccent else NusalingoColors.Disabled))
                }
            }
        }
    }
}
