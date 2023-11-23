package com.example.data.repository

import com.example.data.db.AppDataBase
import com.example.data.entities.ArticlesEntity
import com.example.data.entities.NewsFeedEntity
import com.example.data.entities.SourceEntity
import com.example.data.mapper.NewsFeedEntityContentMapper
import com.example.data.utils.NetworkLogger
import com.example.data.utils.UrlHelper
import com.example.domain.content.ArticlesContent
import com.example.domain.content.NewsFeedContent
import com.example.domain.repository.NewsRepository
import com.example.domain.request_response_state.NetworkResponseState
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


class NewsRepositoryImpl @Inject constructor(
    private val mapper: NewsFeedEntityContentMapper,
    private val database: AppDataBase
) : NewsRepository {
    override suspend fun getNewsFeed(): NetworkResponseState<NewsFeedContent?> {
        var response: NetworkResponseState<NewsFeedContent?>
        try {
            val url = URL(UrlHelper.NewsArticlesFeedUrl)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            NetworkLogger.logUrl(urlConnection.url.toString(), urlConnection.requestMethod)
            try {
                // Read the input stream into a BufferedReader
                val bufferedReader =
                    BufferedReader(InputStreamReader(urlConnection.inputStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (bufferedReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }
                bufferedReader.close()
                val responseString = stringBuilder.toString()
                NetworkLogger.logResponse(
                    responseString,
                    urlConnection.responseCode,
                    urlConnection.url.toString()
                )
                response = NetworkResponseState.success(mapper.transform(parseJson(responseString)))
            } catch (exception: Exception) {
                response = NetworkResponseState.error(exception.localizedMessage ?: "")
                NetworkLogger.logException(exception.localizedMessage ?: "")
            } finally {
                // Disconnect the HttpURLConnection after use
                urlConnection.disconnect()
            }
        } catch (exception: Exception) {
            response = NetworkResponseState.error(exception.localizedMessage ?: "")
            NetworkLogger.logException(exception.localizedMessage ?: "")
        }
        return response
    }

    override suspend fun getAllArticles(): ArrayList<ArticlesContent?> {
        val articlesList = database.getArticlesDao().getAllArticles()
        val articleListContent = arrayListOf<ArticlesContent?>()
        articlesList.forEach {
            articleListContent.add(mapper.transform(it))
        }
        return articleListContent
    }

    override suspend fun insertArticlesList(articlesList: ArrayList<ArticlesContent>): LongArray {
        val articleEntityList = arrayListOf<ArticlesEntity>()
        articlesList.forEach {
            mapper.transform(it)?.let { it1 -> articleEntityList.add(it1) }
        }
        return database.getArticlesDao().insertArticlesList(articleEntityList)
    }

    override suspend fun clearAllArticles() {
        database.getArticlesDao().clearAllArticles()
    }


    private fun parseJson(jsonString: String): NewsFeedEntity {
        val jsonObject = JSONObject(jsonString)
        val status = jsonObject.optString("status")
        val articlesArray = jsonObject.optJSONArray("articles")
        val articlesList = parseArticles(articlesArray)
        return NewsFeedEntity(status, articlesList)
    }

    private fun parseArticles(articlesArray: JSONArray?): ArrayList<ArticlesEntity> {
        val articlesList = arrayListOf<ArticlesEntity>()
        articlesArray?.let {
            for (i in 0 until it.length()) {
                val articleObject = it.getJSONObject(i)
                val sourceObject = articleObject.optJSONObject("source")
                val source = SourceEntity(
                    sourceObject?.optString("id"),
                    sourceObject?.optString("name")
                )
                val author = articleObject.optString("author")
                val title = articleObject.optString("title")
                val description = articleObject.optString("description")
                val url = articleObject.optString("url")
                val imageUrl = articleObject.optString("urlToImage")
                val publishedAt = articleObject.optString("publishedAt")
                val content = articleObject.optString("content")
                val articlesEntity = ArticlesEntity(
                    source,
                    author,
                    title,
                    description,
                    url,
                    imageUrl,
                    publishedAt,
                    content
                )
                articlesList.add(articlesEntity)
            }
        }
        return articlesList
    }
}