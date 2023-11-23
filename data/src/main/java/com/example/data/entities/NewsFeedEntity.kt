package com.example.data.entities

data class NewsFeedEntity(
    var status: String? = null,
    var articles: ArrayList<ArticlesEntity> = arrayListOf()
)