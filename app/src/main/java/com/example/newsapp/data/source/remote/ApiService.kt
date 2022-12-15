package com.example.newsapp.data.source.remote

import com.example.newsapp.models.NewsResponse
import com.example.newsapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("language") language: String?= Constants.DEFAULT_LANG,
        @Query("sortBy") sortParams: String = Constants.SORT_BY,
        @Query("apiKey") api: String = Constants.BASE_URL,
        @Query("page") page: Int,
       @Query("pageSize") pageSize: Int
    ): NewsResponse



}