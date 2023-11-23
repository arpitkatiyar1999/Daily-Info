package com.example.domain.use_case

import com.example.domain.content.ArticlesContent
import com.example.domain.repository.NewsRepository
import javax.inject.Inject

class InsertArticlesListUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend fun invoke(articlesList: ArrayList<ArticlesContent>) =
        repository.insertArticlesList(articlesList)
}