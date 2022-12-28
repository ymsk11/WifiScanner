package com.github.ymatoi.wifiscanner.ui.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ScanErrorDialog(openDialog: Boolean, onClickConfirmButton: () -> Unit) {
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {},
            text = {
                Text("Scan Error")
            },
            confirmButton = {
                TextButton(
                    onClick = onClickConfirmButton
                ) {
                    Text("OK")
                }
            },
            dismissButton = null
        )
    }
}
