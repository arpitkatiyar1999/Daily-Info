package com.example.data.mapper

import com.example.data.entities.ArticlesEntity
import com.example.data.entities.NewsFeedEntity
import com.example.data.entities.SourceEntity
import com.example.domain.content.ArticlesContent
import com.example.domain.content.NewsFeedContent
import com.example.domain.content.SourceContent
import javax.inject.Inject

class NewsFeedEntityContentMapper @Inject constructor() {
    fun transform(newsFeedEntity: NewsFeedEntity?): NewsFeedContent? {
        var newsFeedContent: NewsFeedContent? = null
        newsFeedEntity?.let { response ->
            val articleList = arrayListOf<ArticlesContent>()
            response.articles.forEach { article ->
                transform(article)?.let { articleList.add(it) }
            }
            newsFeedContent = NewsFeedContent(response.status, articleList)
        }
        return newsFeedContent
    }

    fun transform(articlesEntity: ArticlesEntity?): ArticlesContent? {
        var articlesContent: ArticlesContent? = null
        articlesEntity?.let { article ->
            articlesContent = ArticlesContent(
                transform(article.sourceEntity),
                article.author,
                article.title,
                article.description,
                article.url,
                article.urlToImage,
                article.publishedAt,
                article.content
            )
        }
        return articlesContent
    }

    fun transform(articlesContent: ArticlesContent?): ArticlesEntity? {
        var articlesEntity: ArticlesEntity? = null
        articlesContent?.let { article ->
            articlesEntity = ArticlesEntity(
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
        return articlesEntity
    }

    private fun transform(sourceContent: SourceContent?): SourceEntity? {
        var sourceEntity: SourceEntity? = null
        sourceContent?.let { source ->
            sourceEntity = SourceEntity(source.id, source.name)
        }
        return sourceEntity
    }

    private fun transform(sourceEntity: SourceEntity?): SourceContent? {
        var sourceContent: SourceContent? = null
        sourceEntity?.let { source ->
            sourceContent = SourceContent(source.id, source.name)
        }
        return sourceContent
    }
}