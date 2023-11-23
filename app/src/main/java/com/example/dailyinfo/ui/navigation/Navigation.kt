package com.example.dailyinfo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dailyinfo.ui.news.NewsScreen
import com.example.dailyinfo.ui.web_view.WebViewScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.NewsFeedListScreen.route) {
        composable(Screens.NewsFeedListScreen.route) {
            NewsScreen(navController)
        }
        composable(Screens.WebViewScreen.route + "/{url}", arguments = listOf(
            navArgument(name = "url") {
                type = NavType.StringType
            }
        )) { entry ->
            WebViewScreen(url = entry.arguments?.getString("url") ?: "")
        }
    }
}