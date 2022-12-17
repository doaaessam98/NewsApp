package com.example.newsapp.data.source.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.models.Article
import com.example.newsapp.models.Country
import com.example.newsapp.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(article: List<Article>?)

    @Query("SELECT * FROM articles WHERE category = :category  ORDER BY publishedAt DESC")
    fun getArticlesOrderByDate(category: String): PagingSource<Int, Article>

    @Query("SELECT * FROM articles WHERE isFavourite = 1  ORDER BY publishedAt DESC")
    fun getFavArticlesOrderByDate(): Flow<List<Article>>

    @Query("UPDATE  articles set isFavourite = 1 WHERE url =:url")
    suspend fun addFavourite(url:String)

    @Query("SELECT * FROM articles WHERE " + " description LIKE :queryString  AND category =:category AND country =:country " + " ORDER BY publishedAt DESC, name ASC")
    fun searchInArticles(queryString: String,category: String?,country: String?=Constants.DEFULT_COUNTRY):PagingSource<Int, Article>

    @Query("SELECT * FROM articles WHERE " + " description LIKE :queryString  AND category =:category AND country =:country" + " ORDER BY publishedAt DESC, name ASC")
    fun searchInArticles1(queryString: String,category: String?,country: String?=Constants.DEFULT_COUNTRY):Flow<List<Article>>

    @Query("DELETE FROM articles")
    fun clearNews()


}
