package com.example.newsapp.models

sealed class UiAction {
    object  GetNews : UiAction()
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()


}