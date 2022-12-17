package com.example.newsapp.data.repo

import androidx.paging.PagingData
import com.example.newsapp.models.Article
import com.example.newsapp.utils.Result
import kotlinx.coroutines.flow.Flow

interface IRepository {
     fun getArticles(
         query: String,
        country: String?,
        language: String?,
        category: String?,
    ): Flow<PagingData<Article>>



   fun getFavArticle():Flow<List<Article>>
    suspend fun addToFav(url:String)
   suspend fun getSearchDataFromDatabase(
       searchQuery:String,
       country: String,
       category: String
    ): Result<Flow<List<Article>>>

   suspend  fun getSearchData(
        searchQuery: String,
        country: String,
        category: String

    ):Result<Flow<List<Article>>>
}