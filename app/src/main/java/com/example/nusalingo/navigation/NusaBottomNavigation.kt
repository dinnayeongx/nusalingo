package com.example.nusalingo.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nusalingo.ui.theme.NusalingoColors

enum class MainTab { HOME, PRACTICE, LESSON, PROFILE }

@Composable
fun NusaBottomNavigation(selected: MainTab, onSelect: (MainTab) -> Unit) {
    NavigationBar(
        containerColor = NusalingoColors.Background,
        contentColor = NusalingoColors.ActionPrimary,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            selected = selected == MainTab.HOME, onClick = { onSelect(MainTab.HOME) },
            icon = { Icon(Icons.Rounded.Home, contentDescription = "Beranda", modifier = Modifier.size(30.dp)) },
            colors = navColors()
        )
        NavigationBarItem(
            selected = selected == MainTab.PRACTICE, onClick = { onSelect(MainTab.PRACTICE) },
            icon = { Icon(Icons.Outlined.Quiz, contentDescription = "Latihan", modifier = Modifier.size(30.dp)) },
            colors = navColors()
        )
        NavigationBarItem(
            selected = selected == MainTab.LESSON, onClick = { onSelect(MainTab.LESSON) },
            icon = { Icon(Icons.Outlined.Book, contentDescription = "Belajar", modifier = Modifier.size(30.dp)) },
            colors = navColors()
        )
        NavigationBarItem(
            selected = selected == MainTab.PROFILE, onClick = { onSelect(MainTab.PROFILE) },
            icon = { Icon(Icons.Outlined.PersonOutline, contentDescription = "Profil", modifier = Modifier.size(30.dp)) },
            colors = navColors()
        )
    }
}

@Composable
fun navColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = NusalingoColors.BrownAccent,
    unselectedIconColor = NusalingoColors.ActionPrimary,
    indicatorColor = Color.Transparent
)
