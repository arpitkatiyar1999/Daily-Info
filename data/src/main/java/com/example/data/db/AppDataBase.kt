package com.example.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.db.articles.ArticlesDao
import com.example.data.entities.ArticlesEntity


@Database(entities = [ArticlesEntity::class], version = 1)
@TypeConverters(AppTypeConverters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getArticlesDao(): ArticlesDao
}