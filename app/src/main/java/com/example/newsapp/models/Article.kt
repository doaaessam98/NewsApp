package com.example.newsapp.models


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "articles")
data class Article(
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("source")
    @Embedded
    val source: Source,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    @PrimaryKey
    val url: String,
    @SerializedName("urlToImage")
    val urlToImage: String
)