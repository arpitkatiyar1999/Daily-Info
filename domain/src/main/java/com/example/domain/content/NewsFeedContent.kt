package com.example.domain.content

data class NewsFeedContent(
    var status: String? = null,
    var articles: ArrayList<ArticlesContent> = arrayListOf()
)