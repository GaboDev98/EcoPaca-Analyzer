package com.gabodev.ecopacaanalyzer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Toolbar(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    onLogoutClick: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colors.primary,
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            if (showBackButton) {
                Text(
                    text = "←",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier
                        .clickable { onBackClick() }
                        .padding(16.dp)
                )
            } else {
                Spacer(modifier = Modifier.width(16.dp))
            }

            Text(
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.weight(1f)
            )

            if (onLogoutClick != null) {
                Text(
                    text = "⏻",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier
                        .clickable { onLogoutClick() }
                        .padding(8.dp)
                )
            }
        }
    }
}