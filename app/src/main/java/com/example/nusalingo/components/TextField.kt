package com.example.nusalingo.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import com.example.nusalingo.ui.theme.NusalingoColors

enum class NusalingoFieldType {
    EMAIL,
    PASSWORD,
    TEXT
}

@Composable
fun NusalingoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    type: NusalingoFieldType = NusalingoFieldType.TEXT
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val isPassword = type == NusalingoFieldType.PASSWORD

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        placeholder = {
            Text(
                text = placeholder,
                color = NusalingoColors.TextPlaceholder,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            when (type) {
                NusalingoFieldType.EMAIL -> Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Email"
                )
                NusalingoFieldType.PASSWORD -> Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Kata sandi"
                )
                NusalingoFieldType.TEXT -> Unit
            }
        },
        trailingIcon = {
            if (isPassword) {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Outlined.Visibility
                        else
                            Icons.Outlined.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            "Sembunyikan kata sandi"
                        else
                            "Tampilkan kata sandi"
                    )
                }
            }
        },
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = when (type) {
                NusalingoFieldType.EMAIL -> KeyboardType.Email
                NusalingoFieldType.PASSWORD -> KeyboardType.Password
                NusalingoFieldType.TEXT -> KeyboardType.Text
            },
            imeAction = ImeAction.Next
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = NusalingoColors.InputBackground,
            unfocusedContainerColor = NusalingoColors.InputBackground,
            focusedBorderColor = NusalingoColors.ActionSecondary,
            unfocusedBorderColor = NusalingoColors.InputBorder,
            focusedTextColor = NusalingoColors.TextPrimary,
            unfocusedTextColor = NusalingoColors.TextPrimary,
            focusedLeadingIconColor = NusalingoColors.TextSecondary,
            unfocusedLeadingIconColor = NusalingoColors.TextSecondary,
            focusedTrailingIconColor = NusalingoColors.TextSecondary,
            unfocusedTrailingIconColor = NusalingoColors.TextSecondary,
            cursorColor = NusalingoColors.ActionPrimary
        )
    )
}