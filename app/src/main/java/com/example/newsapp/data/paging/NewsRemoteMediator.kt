package com.example.newsapp.data.paging

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.example.newsapp.data.source.local.database.NewsDataBase
import com.example.newsapp.data.source.remote.ApiService
import com.example.newsapp.models.*
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Constants.News_STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val country: String?=Constants.DEFULT_COUNTRY,
    private val language: String?,
    private val category: String?,
    private val service: ApiService,
    private val newsDatabase: NewsDataBase


) : RemoteMediator<Int, Article>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Article>): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: News_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)

                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)

                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)

                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            var response    = service.getNews(country,category,language,page=page, pageSize = state.config.pageSize)




            val article = response.articles
            val endOfPaginationReached = article.isEmpty()
             newsDatabase.withTransaction {
                 if (loadType == LoadType.REFRESH) {
                      newsDatabase.remoteKeysDao().clearRemoteKeys()
                       newsDatabase.NewsDao().clearNews()
                 }
                 val prevKey = if (page== News_STARTING_PAGE_INDEX) null else page - 1
                 val nextKey = if (endOfPaginationReached) null else page + 1
                 val keys = article.map {
                     RemoteKeys(articleUrl = it.url, prevKey = prevKey, nextKey = nextKey)
                 }
                 newsDatabase.remoteKeysDao().insertAll(keys)
                  val databaseArticle=article.toDatabaseModel(category!!,country!!)
                 newsDatabase.NewsDao().insertAll(databaseArticle)
             }
            return MediatorResult.Success(endOfPaginationReached=endOfPaginationReached)
        } catch (exception: IOException) {
            return RemoteMediator.MediatorResult.Error(exception)

        } catch (exception: HttpException) {
            return RemoteMediator.MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { article ->
                newsDatabase.remoteKeysDao().remoteKeysArticleUrl(article.url)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { article ->
                newsDatabase.remoteKeysDao().remoteKeysArticleUrl(article.url)
            }

    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Article>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.url?.let { articleUrl ->
                newsDatabase.remoteKeysDao().remoteKeysArticleUrl(articleUrl)
            }
        }
    }
}