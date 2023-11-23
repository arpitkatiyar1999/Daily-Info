package com.example.dailyinfo.utils

import android.content.Context
import com.example.dailyinfo.R
import com.example.dailyinfo.beans.ArticleSortDataBean
import com.example.dailyinfo.logger.LoggingFunctions
import com.example.dailyinfo.logger.LoggingLevel
import java.text.SimpleDateFormat
import java.util.Locale

object FunctionHelper {
    fun getTimeTextFromDate(dateString: String): String {
        var result = ""
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = sdf.parse(dateString)
            if (date != null) {
                val currentTime = System.currentTimeMillis()
                val timeDifference = currentTime - date.time
                result = when {
                    timeDifference < 60 * 60 * 1000 -> "${timeDifference / (60 * 1000)} min ago"
                    timeDifference < 24 * 60 * 60 * 1000 -> "${timeDifference / (60 * 60 * 1000)} hrs ago"
                    timeDifference < 365 * 24 * 60 * 60 * 1000L -> "${timeDifference / (24 * 60 * 60 * 1000)} days ago"
                    else -> "${timeDifference / (24 * 60 * 60 * 1000 * 365L)} yrs ago"
                }
            }
        } catch (exception: Exception) {
            LoggingFunctions.logData(
                LoggingLevel.Error,
                "DateParseException",
                exception.localizedMessage ?: "",
                "getTimeTextFromDate"
            )
        }
        return result
    }

    //provides the article sort filter , it will provide same filter across the app , so it would be easy to store an retrieve in shared preference
    fun getArticleSortList(context: Context): ArrayList<ArticleSortDataBean> {
        val sortList = arrayListOf<ArticleSortDataBean>()
        sortList.add(
            ArticleSortDataBean(
                1,
                context.getString(R.string.new_to_old),
                context.getString(R.string.most_recent_one_s_will_be_shown_first)
            )
        )
        sortList.add(
            ArticleSortDataBean(
                2,
                context.getString(R.string.old_to_new),
                context.getString(R.string.most_oldest_one_s_will_be_shown_first)
            )
        )
        return sortList
    }
}