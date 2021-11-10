package com.github.ymatoi.wifiscanner.ui.composables

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
