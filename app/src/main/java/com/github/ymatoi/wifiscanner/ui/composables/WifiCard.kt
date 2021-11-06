package com.github.ymatoi.wifiscanner.ui.composables

import android.net.wifi.ScanResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class ScanResultState(
    val ssid: String,
    val bssid: String,
    val frequency: Int,
    val level: Int,
) {
    val hz = when (frequency > 2500) {
        true -> "5GHz"
        else -> "2.4GHz"
    }
    companion object {
        fun create(scanResult: ScanResult) = ScanResultState(
            ssid = scanResult.SSID,
            bssid = scanResult.BSSID,
            frequency = scanResult.frequency,
            level = scanResult.level,
        )
    }
}

@Composable
fun WifiCard(state: ScanResultState) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 10.dp
    ) {
        Column {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("SSID: ${state.ssid} (${state.hz})")
                Text("BSSID: ${state.bssid}")
                Text("level: ${state.level}")
            }
            WifiLevelMeter(
                level = state.level,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewWifiCard() {
    WifiCard(
        state = ScanResultState(
            ssid = "SSID",
            bssid = "BSSID",
            frequency = 2400,
            level = -90,
        )
    )
}
