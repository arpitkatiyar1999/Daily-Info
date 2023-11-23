package com.example.dailyinfo.utils

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferenceHelper @Inject constructor(private val sharedPreferences: SharedPreferences) {
    companion object {
        private const val IsNoRoomDataPresent = "is_no_data_present"
        private const val CurrentSelectedArticleSortFilter = "current_selected_article_sort_filter"
    }

    var isNoRoomDataPresent
        get() = sharedPreferences.getBoolean(IsNoRoomDataPresent, true)
        set(isEntered) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(IsNoRoomDataPresent, isEntered)
            editor.apply()
        }


    var currentSelectedArticleSortFilter
        get() = sharedPreferences.getInt(CurrentSelectedArticleSortFilter, 0)
        set(updatedValue) {
            val editor = sharedPreferences.edit()
            editor.putInt(CurrentSelectedArticleSortFilter, updatedValue)
            editor.apply()
        }

}