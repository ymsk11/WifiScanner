package com.github.ymatoi.wifiscanner.ui.screen

import androidx.lifecycle.ViewModel
import com.github.ymatoi.wifiscanner.repository.WifiScanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wifiScanRepository: WifiScanRepository
) : ViewModel() {
    val scanResults = wifiScanRepository.scanResults
    val buttonEnableState = scanResults.map {
        when (it) {
            is WifiScanRepository.State.Loading -> false
            else -> true
        }
    }
    fun scan() = wifiScanRepository.startScan()
}
