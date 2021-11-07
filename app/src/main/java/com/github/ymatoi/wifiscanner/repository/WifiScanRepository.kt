package com.github.ymatoi.wifiscanner.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.compose.runtime.State
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WifiScanRepository @Inject constructor(
    @ApplicationContext context: Context
) {
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

    private val _scanResults = MutableStateFlow<State?>(null)
    val scanResults: StateFlow<State?> = _scanResults

    fun startScan() {
        _scanResults.value = State.Loading
        wifiManager.startScan()
    }

    private fun scanSuccess() {
        Timber.d("Scan Success")
        _scanResults.value = State.Success(wifiManager.scanResults)
    }

    private fun scanFailure() {
        Timber.d("Scan Failure")
        _scanResults.value = State.Failure
    }

    sealed class State {
        object Loading : State()
        object Failure : State()
        data class Success(val scanResults: List<ScanResult>) : State()
    }
}
