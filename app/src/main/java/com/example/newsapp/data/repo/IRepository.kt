package com.example.newsapp.data.repo

import androidx.paging.PagingData
import com.example.newsapp.models.ApiQuery
import com.example.newsapp.models.Article
import kotlinx.coroutines.flow.Flow

interface IRepository {
     fun getArticles(
        query: ApiQuery,
        country: String?,
        language: String?,
        category: String?,
    ): Flow<PagingData<Article>>


   fun getFavArticle():Flow<List<Article>>
    suspend fun addToFav(url:String)

}