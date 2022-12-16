package com.example.newsapp.models


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import javax.annotation.Nullable
@Entity(tableName = "articles")
data class Article(

    @SerializedName("author")
    val author: String?= "",
    @SerializedName("content")
    val content: String?= "",
    @SerializedName("description")
    val description: String?= "",
    @SerializedName("publishedAt")
    val publishedAt: String="",
    @SerializedName("source")
    @Embedded
    val source: Source? =null,
    @SerializedName("title")
    val title: String? = "'",
    @SerializedName("url")
    @PrimaryKey
    val url: String="",
    @SerializedName("urlToImage")
    val urlToImage: String? = "",
    val isFavourite: Boolean = false,
   val category :String=""
)


fun List<Article>.toDatabaseModel(category:String):List<Article> {
    return map {
        Article(
            author = it.author,
            content = it.content,
            description = it.description,
            publishedAt = it.publishedAt,
            source = it.source,
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage,
            isFavourite = false,
            category=category
        )
    }
        .toMutableList()
}

