package com.example.newsapp.data.repo

import androidx.paging.PagingData
import com.example.newsapp.models.ApiQuery
import com.example.newsapp.models.Article
import kotlinx.coroutines.flow.Flow
import com.example.newsapp.utils.Result

interface IRepository {
    suspend fun getArticles(
        query: ApiQuery,
        country: String?,
        language: String?,
        category: String?,
    ): Result<Flow<PagingData<Article>>>
}