package com.example.newsapp.models

sealed class UiAction {
    data class  GetNews(var category: String) : UiAction()
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentCategory: String) : UiAction()


}