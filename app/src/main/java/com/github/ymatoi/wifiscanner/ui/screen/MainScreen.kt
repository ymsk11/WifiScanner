package com.github.ymatoi.wifiscanner.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.github.ymatoi.wifiscanner.repository.WifiScanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wifiScanRepository: WifiScanRepository
) : ViewModel() {
    val scanResults = wifiScanRepository.scanResults
    fun scan() = wifiScanRepository.startScan()
}

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.scan()
            Timber.d("Granted")
        } else {
            Timber.d("Denied")
        }
    }
    val context = LocalContext.current

    val onClickScan = {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                viewModel.scan()
            }
            else -> {
                launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    val scanResults = viewModel.scanResults.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        val scanResults = scanResults.value
        when (scanResults) {
            is WifiScanRepository.State.Success -> {
                WifiCardList(
                    scanResults = scanResults.scanResults.sortedBy { it.SSID },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
            is WifiScanRepository.State.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().weight(1f))
            }
        }
        Button(onClick = onClickScan) {
            Text("Scan")
        }
    }
}

@Composable
fun WifiCardList(
    scanResults: List<ScanResult>,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier) {
        items(scanResults) {
            WifiCard(it)
        }
    }
}

@Composable
fun WifiCard(scanResult: ScanResult) {
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth(), elevation = 10.dp) {
        Column {
            Text("SSID: ${scanResult.SSID}")
            Text("BSSID: ${scanResult.BSSID}")
            Text("capabilities: ${scanResult.capabilities}")
            Text("centerFreq0: ${scanResult.centerFreq0}")
            Text("centerFreq1: ${scanResult.centerFreq1}")
            Text("channelWidth: ${scanResult.channelWidth}")
            Text("frequency: ${scanResult.frequency}")
            Text("level: ${scanResult.level}")
        }
    }
}

