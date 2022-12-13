package com.example.newsapp.data.source.remote

import com.example.newsapp.models.NewsResponse
import com.example.newsapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String = Constants.DEFAULT_LANG,
        @Query("category") category: String?,
        @Query("apiKey") api: String = Constants.BASE_URL
    ): Response<NewsResponse>



}