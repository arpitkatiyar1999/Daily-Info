package com.example.data.db.articles

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.data.entities.ArticlesEntity

@Dao
interface ArticlesDao {
    @Insert
    fun insertArticlesList(articleList: List<ArticlesEntity>): LongArray

    @Query("SELECT * FROM articles")
    fun getAllArticles(): List<ArticlesEntity>


    @Query("DELETE FROM articles")
    fun clearAllArticles()
}