package com.example.domain.content


data class ArticlesContent(
    var sourceContent: SourceContent? = SourceContent(),
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var url: String? = null,
    var urlToImage: String? = null,
    var publishedAt: String? = null,
    var content: String? = null

)