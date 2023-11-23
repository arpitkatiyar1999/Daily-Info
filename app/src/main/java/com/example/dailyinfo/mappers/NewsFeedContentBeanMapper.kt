package com.example.dailyinfo.mappers

import com.example.dailyinfo.beans.ArticlesBean
import com.example.dailyinfo.beans.SourceBean
import com.example.domain.content.ArticlesContent
import com.example.domain.content.SourceContent
import javax.inject.Inject

class NewsFeedContentBeanMapper @Inject constructor() {


    fun transform(articlesContent: ArticlesContent?): ArticlesBean? {
        var articlesBean: ArticlesBean? = null
        articlesContent?.let { article ->
            articlesBean = ArticlesBean(
                transform(article.sourceContent),
                article.author,
                article.title,
                article.description,
                article.url,
                article.urlToImage,
                article.publishedAt,
                article.content
            )
        }
        return articlesBean
    }

    private fun transform(sourceContent: SourceContent?): SourceBean? {
        var sourceBean: SourceBean? = null
        sourceContent?.let { source ->
            sourceBean = SourceBean(source.id, source.name)
        }
        return sourceBean
    }
}