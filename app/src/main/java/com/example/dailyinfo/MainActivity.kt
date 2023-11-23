package com.example.dailyinfo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.dailyinfo.logger.LoggingFunctions
import com.example.dailyinfo.logger.LoggingLevel
import com.example.dailyinfo.ui.navigation.Navigation
import com.example.dailyinfo.ui.theme.DailyInfoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // handle fcm messages that has both data and notification
        if (intent != null && intent.extras?.keySet()?.isNotEmpty() == true) {
            logNotificationData()
        }
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    //notification permission granted
                    LoggingFunctions.logData(
                        LoggingLevel.Info,
                        "NotificationPermission",
                        "granted",
                        "MainActivity"
                    )
                } else {
                    //permission denied do not block ui , enable normal working flow
                    LoggingFunctions.logData(
                        LoggingLevel.Info,
                        "NotificationPermission",
                        "denied",
                        "MainActivity"
                    )
                }
            }
        setContent {
            DailyInfoTheme {
                Navigation()
            }
        }
    }

    private fun logNotificationData() {
        var message = "{"
        intent?.extras?.keySet()?.forEach { key ->
            message += "\n\"$key\" : \"${intent.getStringExtra(key)}\""
        }
        message += "\n}"
        LoggingFunctions.logData(
            LoggingLevel.Info,
            "NotificationWithData",
            message,
            "MainActivity"
        )
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            val permissionCheckResult = ContextCompat.checkSelfPermission(this, permission)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                // notification permission granted, do not do anything
                LoggingFunctions.logData(
                    LoggingLevel.Info,
                    "NotificationPermission",
                    "granted",
                    "MainActivity"
                )
            } else {
                // Request a permission , do not block if not granted , it will only restrict notification showing on android 13+
                requestPermissionLauncher.launch(permission)
            }
        } else {
            //do not handle it below android 13
        }

    }
}
