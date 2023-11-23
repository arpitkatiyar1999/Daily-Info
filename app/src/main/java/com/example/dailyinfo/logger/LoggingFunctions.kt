package com.example.dailyinfo.logger

import android.util.Log

object LoggingFunctions {
    fun logData(
        loggingLevel: LoggingLevel,
        tag: String,
        message: String,
        loggingPlaceName: String
    ) {
        when (loggingLevel) {
            LoggingLevel.Error -> {
                Log.e(tag, "$loggingPlaceName: $message")
            }

            LoggingLevel.Debug -> {
                Log.d(tag, "$loggingPlaceName: $message")
            }

            LoggingLevel.Info -> {
                Log.i(tag, "$loggingPlaceName: $message")
            }

            LoggingLevel.Warn -> {
                Log.w(tag, "$loggingPlaceName: $message")
            }
        }
    }
}