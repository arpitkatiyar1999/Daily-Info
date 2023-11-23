package com.example.domain.use_case

import com.example.domain.repository.NewsRepository
import javax.inject.Inject

class GetAllArticlesUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend fun invoke() = repository.getAllArticles()
}