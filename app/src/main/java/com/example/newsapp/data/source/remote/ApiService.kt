package com.example.newsapp.data.source.remote

import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.utils.Constants
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface ApiService {

    @Headers("X-Api-Key:${Constants.API_KEY}")
    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("language") language: String?= Constants.DEFAULT_LANG,
        @Query("sortBy") sortParams: String = Constants.SORT_BY,
        @Query("apiKey") api: String = Constants.API_KEY,
        @Query("page") page: Int,
       @Query("pageSize") pageSize: Int
    ): NewsResponse

    @Headers("X-Api-Key:${Constants.API_KEY}")
    @GET("everything")
    suspend fun getSearchNews(
        @Query("q") query: String,
        @Query("sortBy") sortParams: String = Constants.SORT_BY,
        @Query("apiKey") api: String = Constants.API_KEY,
    ): NewsResponse


}