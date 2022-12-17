package com.example.newsapp.models



data class UiState(
    val category: String = "",
    val searchQuery:String = "",
    val lastQueryScrolled: String = "",
    val hasNotScrolledForCurrentCategory: Boolean = false
)
