package com.example.nusalingo.feature.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nusalingo.R
import com.example.nusalingo.feature.auth.ui.ForgotPasswordEvent
import com.example.nusalingo.feature.auth.ui.ForgotPasswordUiState
import com.example.nusalingo.feature.auth.ui.ForgotPasswordViewModel
import com.example.nusalingo.feature.auth.ui.ResetPasswordEvent
import com.example.nusalingo.feature.auth.ui.ResetPasswordUiState
import com.example.nusalingo.feature.auth.ui.ResetPasswordViewModel
import com.example.nusalingo.feature.home.screen.BrandLogo
import com.example.nusalingo.ui.components.ArrowForwardIcon
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
            subtitle = "Kami telah mengirimkan tautan pemulihan ke email di bawah ini. Silakan periksa pesan masukmu"
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
            text = "Belum menemukan emailnya? Periksa folder spam atau pastikan alamat email sudah benar."
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

@Composable
fun ResetPasswordRoute(
    onBack: () -> Unit,
    onBackToLogin: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: ResetPasswordViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                ResetPasswordEvent.Success -> onSuccess()
            }
        }
    }

    ResetPasswordScreen(
        state = state,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmChange = viewModel::onConfirmChange,
        onSubmit = viewModel::submit,
        onBack = onBack,
        onBackToLogin = onBackToLogin,
    )
}

@Composable
fun ResetPasswordScreen(
    state: ResetPasswordUiState,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    onBackToLogin: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    AuthScaffold(
        title = "Kata Sandi Baru",
        onBack = onBack,
        bottomBar = {
            if (state.errorMessage != null) {
                Text(state.errorMessage, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }
            if (state.canSubmit || state.isLoading) {
                PrimaryButton(
                    text = "Simpan kata sandi",
                    onClick = onSubmit,
                    isLoading = state.isLoading
                )
            } else {
                SecondaryButton(
                    text = "Simpan kata sandi",
                    onClick = {},
                    borderColor = NusalingoColors.ActionPrimary,
                    contentColor = NusalingoColors.TextPrimary
                )
            }
            SecondaryButton(text = "Kembali ke masuk", onClick = onBackToLogin, height = 48.dp)
        }
    ) {
        Spacer(Modifier.height(8.dp))
        AuthMascot(res = R.drawable.mascot_lock, size = 240.dp)
        Spacer(Modifier.height(8.dp))
        AuthHeading(
            title = "Buat Kata Sandi Baru",
            subtitle = "Pilih kata sandi yang berbeda dari sebelumnya dan mudah kamu ingat."
        )
        Spacer(Modifier.height(24.dp))
        FieldLabel("Kata sandi baru", color = NusalingoColors.ActionPrimary)
        NusalingoTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = "Masukkan kata sandi baru",
            type = NusalingoFieldType.PASSWORD,
            errorMessage = state.passwordError,
            helperText = "Minimal 8 karakter, dengan huruf dan angka.",
            enabled = !state.isLoading,
        )
        Spacer(Modifier.height(12.dp))
        FieldLabel("Konfirmasi kata sandi")
        NusalingoTextField(
            value = state.confirm,
            onValueChange = onConfirmChange,
            placeholder = "Masukkan ulang kata sandi baru",
            type = NusalingoFieldType.PASSWORD,
            errorMessage = state.confirmError,
            enabled = !state.isLoading,
            imeAction = ImeAction.Done,
            onImeAction = {
                focusManager.clearFocus()
                if (state.canSubmit) onSubmit()
            }
        )
    }
}

@Composable
fun ResetSuccessScreen(onBackToLogin: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NusalingoColors.Background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
    ) {
        BrandLogo()

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AuthMascot(res = R.drawable.mascot, size = 240.dp)
            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(NusalingoColors.ActionSecondary.copy(alpha = 0.15f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = NusalingoColors.ActionSecondary
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "KATA SANDI DIPERBARUI",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NusalingoColors.ActionSecondary
                )
            }

            Spacer(Modifier.height(20.dp))
            AuthHeading(
                title = "Berhasil!",
                subtitle = "Kata sandimu sudah diubah. Masuk kembali dengan kata sandi baru untuk melanjutkan."
            )
        }

        PrimaryButton(
            text = "Kembali ke masuk",
            onClick = onBackToLogin,
            leadingIcon = ArrowForwardIcon
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Jaga kata sandimu. Jangan bagikan kepada siapa pun.",
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = NusalingoColors.TextSecondary
        )
        Spacer(Modifier.height(32.dp))
    }
}