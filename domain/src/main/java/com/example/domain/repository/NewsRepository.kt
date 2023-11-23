package com.example.domain.repository

import com.example.domain.content.ArticlesContent
import com.example.domain.content.NewsFeedContent
import com.example.domain.request_response_state.NetworkResponseState

interface NewsRepository {

    suspend fun getNewsFeed(): NetworkResponseState<NewsFeedContent?>

    suspend fun getAllArticles(): ArrayList<ArticlesContent?>

    suspend fun insertArticlesList(articlesList: ArrayList<ArticlesContent>): LongArray

    suspend fun clearAllArticles()
}