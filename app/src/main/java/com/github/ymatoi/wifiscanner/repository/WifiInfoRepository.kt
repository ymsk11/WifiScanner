package com.github.ymatoi.wifiscanner.repository

import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WifiInfoRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}
