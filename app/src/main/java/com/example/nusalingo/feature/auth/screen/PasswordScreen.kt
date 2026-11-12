package com.example.nusalingo.feature.auth.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.nusalingo.R
import com.example.nusalingo.feature.auth.ui.ForgotPasswordEvent
import com.example.nusalingo.feature.auth.ui.ForgotPasswordUiState
import com.example.nusalingo.feature.auth.ui.ForgotPasswordViewModel
import com.example.nusalingo.ui.components.AuthHeading
import com.example.nusalingo.ui.components.AuthInfoBanner
import com.example.nusalingo.ui.components.AuthMascot
import com.example.nusalingo.ui.components.AuthScaffold
import com.example.nusalingo.ui.components.FieldLabel
import com.example.nusalingo.ui.components.NusalingoFieldType
import com.example.nusalingo.ui.components.NusalingoTextField
import com.example.nusalingo.ui.components.PrimaryButton
import com.example.nusalingo.ui.components.SecondaryButton
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun ForgotPasswordRoute(
    viewModel: ForgotPasswordViewModel,
    onBack: () -> Unit,
    onLinkSent: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ForgotPasswordEvent.LinkSent -> onLinkSent()
            }
        }
    }

    ForgotPasswordScreen(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onSubmit = { viewModel.sendLink() },
        onBack = onBack,
    )
}

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordUiState,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    AuthScaffold(
        title = "Lupa Kata Sandi",
        onBack = onBack,
        bottomBar = {
            if (state.errorMessage != null) {
                Text(state.errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }
            PrimaryButton(
                text = "Kirim tautan reset",
                onClick = onSubmit,
                isLoading = state.isLoading,
            )
            SecondaryButton(text = "Kembali ke masuk", onClick = onBack, height = 48.dp)
        }
    ) {
        Spacer(Modifier.height(8.dp))
        AuthMascot(res = R.drawable.mascot_key, size = 240.dp)
        Spacer(Modifier.height(8.dp))
        AuthHeading(
            title = "Lupa Kata Sandi?",
            subtitle = "Masukkan email akunmu untuk meminta tautan pengaturan ulang."
        )
        Spacer(Modifier.height(24.dp))
        FieldLabel("Email")
        NusalingoTextField(
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = "nama@gmail.com",
            type = NusalingoFieldType.EMAIL,
            errorMessage = state.emailError,
            helperText = "Gunakan email yang terdaftar di NusaLingo.",
            enabled = !state.isLoading,
            imeAction = ImeAction.Done,
            onImeAction = {
                focusManager.clearFocus()
                onSubmit()
            }
        )
        Spacer(Modifier.height(16.dp))
        AuthInfoBanner(
            icon = Icons.Outlined.VerifiedUser,
            text = "Demi keamanan, petunjuk yang sama akan tampil meski email belum terdaftar."
        )
    }
}

@Composable
fun CheckEmailRoute(
    viewModel: ForgotPasswordViewModel,
    onBack: () -> Unit,
    onChangeEmail: () -> Unit,
    onBackToLogin: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CheckEmailScreen(
        state = state,
        onBack = onBack,
        onChangeEmail = onChangeEmail,
        onResend = { viewModel.sendLink(isResend = true) },
        onBackToLogin = onBackToLogin,
    )
}

@Composable
fun CheckEmailScreen(
    state: ForgotPasswordUiState,
    onBack: () -> Unit,
    onChangeEmail: () -> Unit,
    onResend: () -> Unit,
    onBackToLogin: () -> Unit,
) {
    AuthScaffold(
        title = "Periksa Email",
        onBack = onBack,
        bottomBar = {
            SecondaryButton(
                text = if (state.resendSeconds > 0) "Kirim ulang tautan (${state.resendSeconds}d)"
                else "Kirim ulang tautan",
                onClick = onResend,
                enabled = state.canResend,
                borderColor = NusalingoColors.ActionPrimary,
                contentColor = NusalingoColors.TextPrimary
            )
            SecondaryButton(text = "Kembali ke masuk", onClick = onBackToLogin, height = 48.dp)
        }
    ) {
        Spacer(Modifier.height(8.dp))
        AuthMascot(res = R.drawable.mascot_email, size = 240.dp)
        Spacer(Modifier.height(8.dp))
        AuthHeading(
            title = "Cek Email",
            subtitle = "Jika email terdaftar, Firebase akan mengirim tautan reset. Buka tautan itu, ikuti halaman reset, lalu masuk kembali dengan kata sandi baru."
        )
        Spacer(Modifier.height(24.dp))
        NusalingoTextField(
            value = state.email,
            onValueChange = {},
            placeholder = "",
            type = NusalingoFieldType.EMAIL,
            readOnly = true,
        )
        Spacer(Modifier.height(8.dp))
        AuthInfoBanner(
            icon = Icons.Outlined.Info,
            text = "Belum menemukan emailnya? Periksa folder spam. Tautan reset akan dibuka melalui halaman resmi Firebase."
        )
        Text(
            text = "Ubah alamat email",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
                .clickable(onClick = onChangeEmail),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            textDecoration = TextDecoration.Underline,
            color = NusalingoColors.TextPrimary
        )
    }
}