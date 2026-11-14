
package com.example.nusalingo

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import com.example.nusalingo.navigation.MainTab
import com.example.nusalingo.navigation.NusaBottomNavigation
import com.example.nusalingo.navigation.NusalingoNavigation
import com.example.nusalingo.navigation.Routes
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun NusaLingoApp() {
    val navController = rememberNavController()

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    val currentTab = when (currentRoute) {
        Routes.HOME -> MainTab.HOME
        Routes.PRACTICE -> MainTab.PRACTICE
        Routes.LEARN -> MainTab.LESSON
        else -> MainTab.HOME
    }

    val showBottomBar = currentRoute != null &&
            currentRoute !in Routes.authRoutes &&
            !currentRoute.startsWith("practice/quiz")

    Scaffold(
        containerColor = NusalingoColors.Background,
        bottomBar = {
            if (showBottomBar) {
                NusaBottomNavigation(
                    selected = currentTab,
                    onSelect = { tab ->
                        val destination = when (tab) {
                            MainTab.HOME -> Routes.HOME
                            MainTab.PRACTICE -> Routes.PRACTICE
                            MainTab.LESSON -> Routes.LEARN
                            MainTab.PROFILE -> Routes.HOME
                        }

                        navController.navigate(destination) {
                            popUpTo(Routes.HOME) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NusalingoNavigation(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            startDestination = Routes.LOGIN
        )
    }
}
