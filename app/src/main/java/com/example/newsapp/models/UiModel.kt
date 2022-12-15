package com.example.newsapp.models



sealed class UiModel {
    data class ArticleItem(val article: Article) : UiModel()
    data class SeparatorItem(val date: String) : UiModel()

}