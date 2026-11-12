package com.example.nusalingo.navigation

import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.nusalingo.feature.auth.screen.LandingRoute
import com.example.nusalingo.feature.auth.screen.OnboardingRoute
import com.example.nusalingo.feature.auth.screen.RegisterRoute
import com.example.nusalingo.feature.auth.screen.CheckEmailRoute
import com.example.nusalingo.feature.auth.screen.ForgotPasswordRoute
import com.example.nusalingo.feature.auth.screen.LoginRoute
import com.example.nusalingo.feature.auth.ui.ForgotPasswordViewModel

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {

    val goToHome: () -> Unit = {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.LOGIN) { inclusive = true }
            launchSingleTop = true
        }
    }
    val goToLoginLanding: () -> Unit = {
        navController.navigate(Routes.LOGIN_LANDING) {
            popUpTo(Routes.LOGIN) { inclusive = true }
            launchSingleTop = true
        }
    }
    val goHomeFromLoginLanding: () -> Unit = {
        navController.navigate(Routes.HOME) {
            popUpTo(Routes.LOGIN_LANDING) { inclusive = true }
            launchSingleTop = true
        }
    }
    val backToLogin: () -> Unit = {
        navController.popBackStack(Routes.LOGIN, inclusive = false)
    }
    val goToNewAccountLanding: (String) -> Unit = { displayName ->
        navController.navigate(Routes.landing(displayName)) {
            popUpTo(Routes.LOGIN) { inclusive = true }
            launchSingleTop = true
        }
    }

    composable(Routes.LOGIN) {
        LoginRoute(
            onLoginSuccess = { account ->
                if (account.onboardingCompleted) {
                    goToLoginLanding()
                } else {
                    goToNewAccountLanding(account.displayName)
                }
            },
            onForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) },
            onRegister = { navController.navigate(Routes.REGISTER) },
            onContinueAsGuest = goToHome,
        )
    }

    composable(Routes.LOGIN_LANDING) {
        LandingRoute(onContinue = goHomeFromLoginLanding)
    }

    composable(Routes.REGISTER) {
        RegisterRoute(
            onBack = { navController.popBackStack() },
            onLogin = { navController.popBackStack(Routes.LOGIN, inclusive = false) },
            onRegistered = { displayName ->
                goToNewAccountLanding(displayName)
            },
        )
    }

    composable(Routes.LANDING) { entry ->
        val displayName = entry.arguments?.getString("displayName").orEmpty()
        LandingRoute(
            onContinue = {
                navController.navigate(Routes.onboarding(displayName)) {
                    popUpTo(Routes.LANDING) { inclusive = true }
                    launchSingleTop = true
                }
            },
        )
    }

    composable(Routes.ONBOARDING) {
        OnboardingRoute(
            onCompleted = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                    launchSingleTop = true
                }
            },
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

}