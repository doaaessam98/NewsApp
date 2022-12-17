package com.example.newsapp.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapp.models.Article
import com.example.newsapp.models.RemoteKeys

@Database(
    entities = [Article::class,RemoteKeys::class],
    version = 6,
    exportSchema = false
)
abstract class NewsDataBase :RoomDatabase() {
    abstract fun NewsDao(): NewsDao
    abstract fun remoteKeysDao(): RemoteKeysDao

}