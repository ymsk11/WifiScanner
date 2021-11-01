package com.github.ymatoi.wifiscanner.ui.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    val message = "Hello, World"
}

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    Text("Hello World")
}