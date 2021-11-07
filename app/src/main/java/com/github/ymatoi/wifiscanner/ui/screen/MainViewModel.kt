package com.github.ymatoi.wifiscanner.ui.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ymatoi.wifiscanner.repository.WifiScanRepository
import com.github.ymatoi.wifiscanner.ui.composables.ScanResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wifiScanRepository: WifiScanRepository,
) : ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _scanResultStates: MutableState<List<ScanResultState>> = mutableStateOf(emptyList())
    val scanResultStates: State<List<ScanResultState>> = _scanResultStates

    init {
        wifiScanRepository.scanResults.onEach {
            when (it) {
                is WifiScanRepository.State.Loading -> {
                    _isLoading.value = true
                }
                is WifiScanRepository.State.Success -> {
                    _isLoading.value = false
                    _scanResultStates.value = it.scanResults.map {
                        ScanResultState.create(it)
                    }.sortedBy { it.ssid }
                }
                is WifiScanRepository.State.Failure -> {
                    _isLoading.value = false
                }
            }
        }.launchIn(viewModelScope)
    }
    fun scan() = wifiScanRepository.startScan()
}
