package com.example.newsapp.ui.search

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.repo.IRepository
import com.example.newsapp.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.newsapp.utils.Result


@HiltViewModel
class SearchViewModel @Inject constructor(private  val repository: IRepository):ViewModel() {

    private  val _search =MutableStateFlow<List<Article>>(listOf())
     val search :StateFlow<List<Article>>
      get() = _search



init {

    getSearchData("","","")
}

     fun getSearchData(
        searchQuery: String ,
        country: String = "",
        category:String=""
    ) {
        Log.e(TAG, "getSearchData: $searchQuery", )
       viewModelScope.launch {
           repository.getSearchData(searchQuery,country,category).let {
               when(it){
                   is Result.Error->{

                   }
                   is Result.Success->{
                        it.data.collect{
                            _search.emit(it)
                        }

                   }
                   else->{

                   }
               }
           }
       }
    }


}