package com.github.ymatoi.wifiscanner.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartMessage() {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Swipe down to scan Wi-Fi APs",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "Allow WifiScanner to access your device's fine location because of scanning Wi-Fi APs.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
