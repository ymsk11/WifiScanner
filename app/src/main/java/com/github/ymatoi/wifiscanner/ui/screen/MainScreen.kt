package com.github.ymatoi.wifiscanner.ui.screen

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {
    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)) {
                true -> scanSuccess()
                else -> scanFailure()
            }
        }
    }

    private val intentFilter = IntentFilter().also {
        it.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    }

    init {
        context.registerReceiver(wifiScanReceiver, intentFilter)
    }

    fun scan() {
        wifiManager.startScan()
    }

    private val _scanResults: MutableState<List<ScanResult>> = mutableStateOf(emptyList())
    val scanResults: State<List<ScanResult>> = _scanResults

    private fun scanSuccess() {
        Timber.d("Scan Success")
        _scanResults.value = wifiManager.scanResults
    }

    private fun scanFailure() {
        Timber.d("Scan Failure")
    }
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

    val scanResults by viewModel.scanResults

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(scanResults) {
                Timber.d("scanResult $it")
                Text(text = it.SSID)
            }
        }
        Button(onClick = onClickScan) {
            Text("Scan")
        }
    }
}
