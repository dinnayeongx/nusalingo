package com.example.nusalingo.feature.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nusalingo.R
import com.example.nusalingo.feature.auth.data.AccountLoginResult
import com.example.nusalingo.feature.auth.ui.LoginEvent
import com.example.nusalingo.feature.auth.ui.LoginUiState
import com.example.nusalingo.feature.auth.ui.LoginViewModel
import com.example.nusalingo.feature.home.screen.BrandLogo
import com.example.nusalingo.ui.components.ArrowForwardIcon
import com.example.nusalingo.ui.components.AuthHeading
import com.example.nusalingo.ui.components.AuthMascot
import com.example.nusalingo.ui.components.FieldLabel
import com.example.nusalingo.ui.components.NusalingoFieldType
import com.example.nusalingo.ui.components.NusalingoTextField
import com.example.nusalingo.ui.components.PrimaryButton
import com.example.nusalingo.ui.components.SecondaryButton
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun LoginRoute(
    onLoginSuccess: (AccountLoginResult) -> Unit,
    onForgotPassword: () -> Unit,
    onRegister: () -> Unit,
    onContinueAsGuest: () -> Unit,
    viewModel: LoginViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.LoginSuccess -> onLoginSuccess(event.account)
            }
        }
    }

    LoginScreen(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::login,
        onForgotPasswordClick = onForgotPassword,
        onRegisterClick = onRegister,
        onGuestClick = onContinueAsGuest,
    )
}

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onGuestClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
    ) {
        BrandLogo()
        AuthMascot(res = R.drawable.mascot, size = 180.dp)
        Spacer(Modifier.height(12.dp))
        AuthHeading(
            title = "Halo Selamat Datang!",
            subtitle = "Masuk untuk melanjutkan petualangan aksaramu.",
            centered = false
        )

        Spacer(Modifier.height(20.dp))
        FieldLabel("Email")
        NusalingoTextField(
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = "nama@gmail.com",
            type = NusalingoFieldType.EMAIL,
            errorMessage = state.emailError,
            enabled = !state.isLoading,
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(Modifier.height(8.dp))
        FieldLabel("Kata sandi")
        NusalingoTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = "Minimal 8 karakter",
            type = NusalingoFieldType.PASSWORD,
            errorMessage = state.passwordError,
            enabled = !state.isLoading,
            imeAction = ImeAction.Done,
            onImeAction = {
                focusManager.clearFocus()
                onLoginClick()
            }
        )

        Text(
            text = "Lupa kata sandi?",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .clickable(onClick = onForgotPasswordClick),
            textAlign = TextAlign.End,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold,
            color = NusalingoColors.ActionSecondary
        )

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        PrimaryButton(
            text = "Masuk",
            onClick = onLoginClick,
            isLoading = state.isLoading,
            leadingIcon = ArrowForwardIcon
        )

        Spacer(Modifier.height(16.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = NusalingoColors.TextSecondary)) {
                    append("Belum punya akun? ")
                }
                withStyle(
                    SpanStyle(
                        color = NusalingoColors.ActionSecondary,
                        fontWeight = FontWeight.ExtraBold
                    )
                ) { append("Daftar") }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onRegisterClick),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(24.dp))
        SecondaryButton(
            text = "Lanjut tanpa akun",
            onClick = onGuestClick,
            enabled = !state.isLoading,
            contentColor = NusalingoColors.TextPrimary
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Tanpa akun. Progres belajarmu tersimpan di perangkat ini.",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = NusalingoColors.TextSecondary
        )
    }
}