package com.example.nusalingo.feature.auth.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nusalingo.feature.auth.ui.RegisterEvent
import com.example.nusalingo.feature.auth.ui.RegisterUiState
import com.example.nusalingo.feature.auth.ui.RegisterViewModel
import com.example.nusalingo.ui.components.ArrowForwardIcon
import com.example.nusalingo.ui.components.AuthHeading
import com.example.nusalingo.ui.components.AuthScaffold
import com.example.nusalingo.ui.components.FieldLabel
import com.example.nusalingo.ui.components.NusalingoFieldType
import com.example.nusalingo.ui.components.NusalingoTextField
import com.example.nusalingo.ui.components.PrimaryButton
import com.example.nusalingo.ui.theme.NusalingoColors

@Composable
fun RegisterRoute(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onRegistered: (String) -> Unit,
    viewModel: RegisterViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is RegisterEvent.Registered -> onRegistered(event.displayName)
            }
        }
    }

    RegisterScreen(
        state = state,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onAcceptedTermsChange = viewModel::onAcceptedTermsChange,
        onRegister = viewModel::register,
        onBack = onBack,
        onLogin = onLogin,
    )
}

@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onAcceptedTermsChange: (Boolean) -> Unit,
    onRegister: () -> Unit,
    onBack: () -> Unit,
    onLogin: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    AuthScaffold(
        title = "Daftar akun",
        onBack = onBack,
        bottomBar = {
            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                )
            }
            PrimaryButton(
                text = "Buat akun",
                onClick = onRegister,
                isLoading = state.isLoading,
                leadingIcon = if (state.isLoading) null else ArrowForwardIcon,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Sudah punya akun? ", color = NusalingoColors.TextSecondary, fontSize = 14.sp)
                Text(
                    text = "Masuk",
                    modifier = Modifier.clickable(
                        enabled = !state.isLoading,
                        onClick = onLogin,
                    ),
                    color = NusalingoColors.ActionSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }
        },
    ) {
        Spacer(Modifier.height(8.dp))
        AuthHeading(
            title = "Mulai langkah barumu",
            subtitle = "Isi data berikut untuk membuat akun dan menyimpan progres belajarmu.",
            centered = false,
        )
        Spacer(Modifier.height(22.dp))

        FieldLabel("Nama")
        NusalingoTextField(
            value = state.name,
            onValueChange = onNameChange,
            placeholder = "Nama Akunmu",
            type = NusalingoFieldType.TEXT,
            errorMessage = state.nameError,
            enabled = !state.isLoading,
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
        )
        Spacer(Modifier.height(8.dp))

        FieldLabel("Email")
        NusalingoTextField(
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = "nama@email.com",
            type = NusalingoFieldType.EMAIL,
            errorMessage = state.emailError,
            helperText = if (state.emailError == null) "Gunakan email yang bisa kamu akses." else null,
            enabled = !state.isLoading,
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
        )
        Spacer(Modifier.height(8.dp))

        FieldLabel("Kata sandi")
        NusalingoTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = "Minimal 8 karakter",
            type = NusalingoFieldType.PASSWORD,
            errorMessage = state.passwordError,
            helperText = if (state.passwordError == null) {
                "Gunakan huruf besar, huruf kecil, angka, dan simbol."
            } else {
                null
            },
            enabled = !state.isLoading,
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
        )
        Spacer(Modifier.height(8.dp))

        FieldLabel("Konfirmasi kata sandi")
        NusalingoTextField(
            value = state.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = "Masukkan ulang kata sandi",
            type = NusalingoFieldType.PASSWORD,
            errorMessage = state.confirmPasswordError,
            enabled = !state.isLoading,
            imeAction = ImeAction.Done,
            onImeAction = {
                focusManager.clearFocus()
                onRegister()
            },
        )
        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = state.acceptedTerms,
                onCheckedChange = onAcceptedTermsChange,
                enabled = !state.isLoading,
                colors = CheckboxDefaults.colors(
                    checkedColor = NusalingoColors.ActionPrimary,
                    uncheckedColor = NusalingoColors.ActionPrimary,
                ),
            )
            Text(
                text = buildAnnotatedString {
                    append("Saya menyetujui ")
                    withStyle(SpanStyle(color = NusalingoColors.ActionSecondary, fontWeight = FontWeight.Bold)) {
                        append("Ketentuan Penggunaan")
                    }
                    append(" dan ")
                    withStyle(SpanStyle(color = NusalingoColors.ActionSecondary, fontWeight = FontWeight.Bold)) {
                        append("Kebijakan Privasi")
                    }
                    append(" NusaLingo.")
                },
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        enabled = !state.isLoading,
                        onClick = { onAcceptedTermsChange(!state.acceptedTerms) },
                    ),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = NusalingoColors.TextSecondary,
            )
        }
        if (state.termsError != null) {
            Text(
                text = state.termsError,
                modifier = Modifier.padding(start = 12.dp),
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}
