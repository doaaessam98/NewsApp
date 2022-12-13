package com.example.newsapp.data.source.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.models.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(article: List<Article>?)

    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getArticlesOrderByDate(): PagingSource<Int, Article>

    @Query("SELECT * FROM articles WHERE " + " description LIKE :queryString " + "ORDER BY publishedAt DESC, name ASC")
    fun searchInArticles(queryString: String): PagingSource<Int, Article>

    @Query("DELETE FROM articles")
    fun clearNews()


}
