package com.example.data.utils

import android.util.Log

object NetworkLogger {
    private const val TAG = "NetworkLogger"
    fun logUrl(url: String, requestMethod: String) {
        Log.i(TAG, "$requestMethod $url")
    }

    fun logResponse(response: String, responseCode: Int, url: String) {
        Log.i(TAG, "$responseCode $url\n$response ")
    }

    fun logException(exceptionMessage: String) {
        Log.e(TAG, exceptionMessage)
    }
}