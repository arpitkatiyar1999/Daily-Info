package com.example.dailyinfo.ui.navigation

sealed class Screens(val route: String) {
    object NewsFeedListScreen : Screens("news_feed_list")
    object WebViewScreen : Screens("web_view")
}
