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
import kotlinx.coroutines.flow.flow

class Repository @Inject constructor(
    private  val newsDataBase: NewsDataBase,
    private val  apiService: ApiService
    ):IRepository {


    override fun getArticles(
        query: String,
        country: String?,
        language: String?,
        category: String?,
    ): Flow<PagingData<Article>> {

        val pagingSourceFactory = {
                  when{

                    query.isEmpty()->{ newsDataBase.NewsDao().getArticlesOrderByDate(category!!)}

                      else ->{ newsDataBase.NewsDao().searchInArticles(query,category,country)
                     }
                  }

        }


        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = NewsRemoteMediator(
                country,
                language,
                category,
                apiService,
                newsDataBase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow


    }





    override fun getFavArticle(): Flow<List<Article>> {
        return newsDataBase.NewsDao().getFavArticlesOrderByDate()
    }

    override suspend fun addToFav(url: String) {
        newsDataBase.NewsDao().addFavourite(url)
    }


    override suspend fun getSearchData(
        searchQuery: String,
        country: String,
        category: String

    ): Result<Flow<List<Article>>> {
          return  try {
                val articleSearchResult = getSearchDataFromApi(searchQuery, country, category)
                   Result.Success(articleSearchResult)
            } catch (e: Exception) {
                //getSearchDataFromDatabase(searchQuery, country, category)
                Result.Error(e)
            }
        }





    fun getSearchDataFromApi(
        searchQuery:String,
        country: String,
        category: String
    ): Flow<List<Article>> = flow {
        emit(apiService.getSearchNews(query = searchQuery).articles)
    }
    override suspend fun getSearchDataFromDatabase(
        searchQuery:String,
        country: String,
        category: String
    ) :Result<Flow<List<Article>>>{

        return try {
            val articleSearchResult =
                newsDataBase.NewsDao().searchInArticles1(searchQuery,category,country)
            Result.Success(articleSearchResult)
        }catch (e:Exception){
            Result.Error(e)
        }

    }
}

