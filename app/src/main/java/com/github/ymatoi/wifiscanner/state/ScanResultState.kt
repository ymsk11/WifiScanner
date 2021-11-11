package com.github.ymatoi.wifiscanner.state

import android.net.wifi.ScanResult

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
