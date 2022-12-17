package com.example.newsapp.ui.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.newsapp.data.repo.IRepository
import com.example.newsapp.models.UiAction
import com.example.newsapp.models.UiModel
import com.example.newsapp.models.UiState
import com.example.newsapp.utils.Constants
import com.example.newsapp.utils.Constants.LAST_SEARCH_QUERY
import com.example.newsapp.utils.Constants.LAST_CATEGORY_SELECTED
import com.example.newsapp.utils.Constants.LAST_QUERY_SCROLLED
import com.example.newsapp.utils.dateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IRepository,
    private val savedStateHandle: SavedStateHandle
):ViewModel() {


   // val topHeadlinesFlow  : StateFlow<PagingData<UiModel>>
    var selectedCategory :String
    val state :StateFlow<UiState>
    val articlePagingDataFlow: Flow<PagingData<UiModel>>
    val onCategoryChange: (UiAction) -> Unit
    val onSearchChange:(UiAction)->Unit





    init {
        selectedCategory = savedStateHandle[LAST_CATEGORY_SELECTED] ?:Constants. DEFAULT_CATEGORY
        val initialSearchQuery = savedStateHandle[LAST_SEARCH_QUERY] ?: Constants.DEFAULT_SEARCH_QUERY
        val lastQueryScrolled: String = savedStateHandle[LAST_QUERY_SCROLLED] ?: Constants.DEFAULT_CATEGORY
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val getHeadlines = actionStateFlow
            .filterIsInstance<UiAction.GetNews>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.GetNews(category = selectedCategory)) }

        val search = actionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(initialSearchQuery)) }

        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(currentCategory = lastQueryScrolled)) }


         articlePagingDataFlow =
             combine(
                 getHeadlines,
                 search,::Pair
             ).map {(headline,search)->{
                 getArticles(search.query,Constants.DEFULT_COUNTRY,"",headline.category)
             }}.flatMapLatest {
                 it.invoke()
             }.cachedIn(viewModelScope)




        state = combine(
            getHeadlines,
            queriesScrolled,
            search,
            ::Triple
        ).map { (headline, scroll,search) ->
            UiState(
                category = headline.category,
                searchQuery =search.query ,
                lastQueryScrolled = scroll.currentCategory, hasNotScrolledForCurrentCategory = headline.category!= scroll.currentCategory
            )
        }

            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 1000),
                initialValue = UiState()
            )

        onCategoryChange = { action ->
            Log.e(TAG, "$action: ccc", )

            viewModelScope.launch { actionStateFlow.emit(action) }
        }

        onSearchChange = { action ->
            Log.e(TAG, "$action: sss", )
            viewModelScope.launch { actionStateFlow.emit(action) }
        }

    }



    private fun getArticles(
        query: String,
        country: String?,
        language: String?,
        category: String?): Flow<PagingData<UiModel>> {
        Log.e(TAG, "getArticles:${query}and ${category} ", )
       return repository.getArticles(
            query,
             language =language,
            category =category,
            country = country).map { pagingData ->
              pagingData.map {
                  UiModel.ArticleItem(it) }}.map {

                  it.insertSeparators { before,after->


                if (after == null) {
                    return@insertSeparators null
                }

                if (before == null) {
                    return@insertSeparators UiModel.SeparatorItem(after.article.publishedAt)
                }
                // check between 2 items
                     val com = dateFormat(before.article.publishedAt)!!.compareTo(dateFormat(after.article.publishedAt)!!)
                      if (com >0) {

                        UiModel.SeparatorItem(after.article.publishedAt)

                } else {

                    null
                }
                  }
        }



    }

    fun addToFavourite(articleUrl:String){
        viewModelScope.launch {
            repository.addToFav(url = articleUrl)
        }

    }
    override fun onCleared() {
        savedStateHandle[LAST_CATEGORY_SELECTED] = state.value.category
        savedStateHandle[LAST_SEARCH_QUERY] = state.value.searchQuery
        savedStateHandle[LAST_QUERY_SCROLLED] = state.value.lastQueryScrolled
        super.onCleared()
    }




}

