package com.gabodev.iotdevicemonitor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gabodev.iotdevicemonitor.ui.theme.Spacing

internal fun isValidEmail(email: String) = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matches(email.trim())

/** Email field used by every auth screen. */
@Composable
internal fun EmailField(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    showError: Boolean,
    imeAction: ImeAction = ImeAction.Next,
) {
    val invalid = showError && value.isNotEmpty() && !isValidEmail(value)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Email") },
        placeholder = { Text("tu@email.com") },
        leadingIcon = { Icon(Icons.Rounded.Mail, contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = imeAction),
        singleLine = true,
        enabled = enabled,
        isError = invalid,
        supportingText = if (invalid) ({ Text("Ingresa un email válido") }) else null,
        modifier = Modifier.fillMaxWidth(),
    )
}

/** Filled primary button with the loading state from the design system. */
@Composable
internal fun PrimaryButton(text: String, loadingText: String, loading: Boolean, enabled: Boolean, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = enabled && !loading, modifier = Modifier.fillMaxWidth()) {
        if (loading) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp, color = LocalContentColor.current)
                Text(loadingText)
            }
        } else {
            Text(text)
        }
    }
}
