package com.example.nusalingo.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.nusalingo.feature.auth.screen.CheckEmailRoute
import com.example.nusalingo.feature.auth.screen.ForgotPasswordRoute
import com.example.nusalingo.feature.auth.screen.LoginRoute
import com.example.nusalingo.feature.auth.screen.ResetPasswordRoute
import com.example.nusalingo.feature.auth.screen.ResetSuccessScreen
import com.example.nusalingo.feature.auth.ui.ForgotPasswordViewModel

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {

    val goToHome: () -> Unit = {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.LOGIN) { inclusive = true }
            launchSingleTop = true
        }
    }
    val backToLogin: () -> Unit = {
        navController.popBackStack(Routes.LOGIN, inclusive = false)
    }

    composable(Routes.LOGIN) {
        LoginRoute(
            onLoginSuccess = goToHome,
            onForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
            onRegister = { /* TODO: navigate ke layar Daftar */ },
            onContinueAsGuest = goToHome,
        )
    }

    composable(Routes.FORGOT_PASSWORD) { entry ->
        val vm: ForgotPasswordViewModel = viewModel(entry)
        ForgotPasswordRoute(
            viewModel = vm,
            onBack = { navController.popBackStack() },
            onLinkSent = { navController.navigate(Routes.CHECK_EMAIL) },
        )
    }

    composable(Routes.CHECK_EMAIL) { entry ->
        val parentEntry = remember(entry) {
            navController.getBackStackEntry(Routes.FORGOT_PASSWORD)
        }
        val vm: ForgotPasswordViewModel = viewModel(parentEntry)
        CheckEmailRoute(
            viewModel = vm,
            onBack = { navController.popBackStack() },
            onChangeEmail = { navController.popBackStack() },
            onBackToLogin = backToLogin,
        )
    }

    composable(
        route = Routes.RESET_PASSWORD,
        deepLinks = listOf(navDeepLink { uriPattern = "nusalingo://reset-password" })
    ) {
        ResetPasswordRoute(
            onBack = { navController.popBackStack() },
            onBackToLogin = {
                if (!navController.popBackStack(Routes.LOGIN, inclusive = false)) {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.RESET_PASSWORD) { inclusive = true }
                    }
                }
            },
            onSuccess = {
                navController.navigate(Routes.RESET_SUCCESS) {
                    popUpTo(Routes.RESET_PASSWORD) { inclusive = true }
                }
            },
        )
    }

    composable(Routes.RESET_SUCCESS) {
        ResetSuccessScreen(
            onBackToLogin = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.RESET_SUCCESS) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}