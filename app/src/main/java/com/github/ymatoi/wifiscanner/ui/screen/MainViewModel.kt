package com.github.ymatoi.wifiscanner.ui.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ymatoi.wifiscanner.repository.WifiScanRepository
import com.github.ymatoi.wifiscanner.state.ScanResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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

    private val _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError
    fun closeErrorDialog() { _isError.value = false }

    private val _scanResultStates = MutableStateFlow<List<ScanResultState>>(emptyList())

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword

    val scanResultStates = _scanResultStates.combine(_searchKeyword) { result, keyword ->
        result.filter { it.ssid.contains(keyword) }.sortedByDescending { it.level }
    }

    fun updateSearchKeyword(text: String) {
        _searchKeyword.value = text
    }

    init {
        wifiScanRepository.scanResults.onEach {
            when (it) {
                is WifiScanRepository.State.Loading -> {
                    _isLoading.value = true
                    _isError.value = false
                }
                is WifiScanRepository.State.Success -> {
                    _isLoading.value = false
                    _isError.value = false
                    _scanResultStates.value = it.scanResults.map {
                        ScanResultState.create(it)
                    }
                }
                is WifiScanRepository.State.Failure -> {
                    _isLoading.value = false
                    _isError.value = true
                }
            }
        }.launchIn(viewModelScope)
    }
    fun scan() = wifiScanRepository.startScan()
}
