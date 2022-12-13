package com.example.newsapp.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.newsapp.data.paging.NewsRemoteMediator
import com.example.newsapp.data.source.local.database.NewsDataBase
import com.example.newsapp.data.source.remote.ApiService
import com.example.newsapp.models.ApiQuery
import com.example.newsapp.models.Article
import com.example.newsapp.utils.Constants.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.newsapp.utils.Result

class Repository @Inject constructor(
    private  val newsDataBase: NewsDataBase,
    private val apiService: ApiService
    ):IRepository{



     override suspend fun getArticles(
         query: ApiQuery,
         country: String?,
        language: String?,
         category: String?,
     ): Result<Flow<PagingData<Article>>> {

        val pagingSourceFactory ={
            when (query) {
                is ApiQuery.GetAll ->newsDataBase.NewsDao().getArticlesOrderByDate()
                is ApiQuery.Search ->newsDataBase.NewsDao().searchInArticles(query.query)

            }

        }

        @OptIn(ExperimentalPagingApi::class)
        return  try {

            val result= Pager(
                config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
                remoteMediator = NewsRemoteMediator(
                    query,
                    country,
                    language,
                    category,
                    apiService,
                    newsDataBase
                ),
                pagingSourceFactory = pagingSourceFactory
            ).flow
            Result.Success(result)
        }catch (e:Exception){
            Result.Error(e)
        }
    }
}