package com.github.ymatoi.wifiscanner.ui.composables

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.ymatoi.wifiscanner.state.ScanResultState

@Composable
fun WifiCardList(
    scanResultStates: List<ScanResultState>,
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
}
