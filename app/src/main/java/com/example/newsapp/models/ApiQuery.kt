package com.example.newsapp.models

sealed class ApiQuery{
    class Search(val query: String) : ApiQuery()
    object GetAll : ApiQuery()
}
