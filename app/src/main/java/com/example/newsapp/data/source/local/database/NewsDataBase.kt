package com.example.newsapp.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDataBase :RoomDatabase() {
    abstract fun NewsDao(): NewsDao

}