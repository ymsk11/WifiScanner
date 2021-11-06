package com.github.ymatoi.wifiscanner.ui.composables

import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WifiLevelMeter(level: Int, modifier: Modifier = Modifier) {
    val progress = (level + 100f) / 100f
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier,
        color = Color.Green,
        backgroundColor = Color.Gray
    )
}

@Preview
@Composable
fun PreviewWifiLevelMeter() {
    WifiLevelMeter(
        level = -50,
        modifier = Modifier
            .height(24.dp)
    )
}
