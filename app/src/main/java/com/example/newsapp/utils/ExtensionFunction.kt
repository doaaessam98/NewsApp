package com.example.newsapp.utils

import android.content.Context
import com.example.newsapp.models.Category
import com.example.newsapp.models.Country
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.toList
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass


fun <T:Any>readFromAsset(file_name:String,context: Context,model: KClass<T>): List<T>? {
     val bufferReader = context.assets.open(file_name).bufferedReader()

    val json_string = bufferReader.use {
        it.readText()
    }
    val gson = Gson()

    val modelList: List<T>? =  when(model) {
        Country::class -> gson.fromJson(json_string, Array<Country>::class.java ).toList() as List<T>
        Category::class -> gson.fromJson(json_string, Array<Category>::class.java ).toList() as List<T>
        else -> null
    }

     return modelList
}




 fun List<String>.toCategoryList(): List<Category> {
    var categoryList:List<Category> = listOf()
    this.forEach {
        categoryList =  categoryList.plus(Category(it,""))
    }
    return categoryList
}




fun dateFormat(date: String?): String? {
    val newDate: String?
    val dateFormat = SimpleDateFormat("E, d MMM yyyy", getCountry()?.let { Locale(it) })
    newDate = try {
        val date: Date = date?.let {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).parse(it) } as Date

           dateFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
         date
    }
    return newDate
}

fun getCountry(): String? {
    val locale = Locale.getDefault()
    val country = locale.country
    return country.lowercase(Locale.getDefault())
}