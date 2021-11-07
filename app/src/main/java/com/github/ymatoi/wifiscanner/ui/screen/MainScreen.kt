package com.github.ymatoi.wifiscanner.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.ymatoi.wifiscanner.ui.composables.ScanResultState
import com.github.ymatoi.wifiscanner.ui.composables.WifiCard
import timber.log.Timber

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

    val isLoading = viewModel.isLoading
    val scanResultStates = viewModel.scanResultStates

    MainScreen(
        isLoading = isLoading.value,
        scanResultStates = scanResultStates.value,
        onClickScan = onClickScan
    )
}

@Composable
fun MainScreen(isLoading: Boolean, scanResultStates: List<ScanResultState>, onClickScan: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                items(scanResultStates) {
                    WifiCard(state = it)
                }
            }
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                        .align(Alignment.Center)
                )
            }
        }
        Button(onClick = onClickScan, enabled = isLoading.not()) {
            Text("Scan")
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen(
        isLoading = false,
        scanResultStates = emptyList(),
        onClickScan = {}
    )
}

@Preview
@Composable
fun PreviewMainScreenLoading() {
    MainScreen(
        isLoading = true,
        scanResultStates = emptyList(),
        onClickScan = {}
    )
}
