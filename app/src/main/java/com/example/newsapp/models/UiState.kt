package com.example.newsapp.models



data class UiState(
    val category: String = "",
    val lastQueryScrolled: String = "",
    val hasNotScrolledForCurrentCategory: Boolean = false
)
