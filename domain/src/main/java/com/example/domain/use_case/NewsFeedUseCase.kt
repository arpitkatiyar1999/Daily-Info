package com.example.domain.use_case

import com.example.domain.repository.NewsRepository
import javax.inject.Inject

class NewsFeedUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend fun invoke() = repository.getNewsFeed()



}