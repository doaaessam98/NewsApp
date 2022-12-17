package com.example.newsapp.ui.favourite

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.repo.IRepository
import com.example.newsapp.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FavouriteViewModel  @Inject constructor(private val repository: IRepository):ViewModel(){

        val emptyList = MutableLiveData(true)
      private val _favourite = MutableStateFlow<List<Article>>(listOf())
        val  favourite : StateFlow<List<Article>> = _favourite

     init {
         getFavouriteArticle()
     }
    private  fun getFavouriteArticle(){
        viewModelScope.launch {
            repository.getFavArticle().let {
                 it.collect{
                     Log.e(TAG, "getFavouriteArticle: ${it.size}", )
                     when{
                         it.isNotEmpty()-> {
                             _favourite.emit(it)
                             emptyList.value = (false)
                         }

                     }
                 }
            }
        }
    }
}