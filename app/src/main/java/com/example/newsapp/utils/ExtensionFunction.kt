package com.example.newsapp.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.newsapp.models.Category
import com.example.newsapp.models.Country
import com.google.gson.Gson
import kotlin.reflect.KClass


 fun <T:Any>readFromAsset(file_name:String,context: Context,model: KClass<T>): List<T>? {
     val bufferReader = context.assets.open(file_name).bufferedReader()

    val json_string = bufferReader.use {
        it.readText()
    }
     Log.e(TAG, "readFromAsset: ${json_string}", )
    val gson = Gson()

    val modelList: List<T>? =  when(model) {
        Country::class -> gson.fromJson(json_string, Array<Country>::class.java ).toList() as List<T>
        Category::class -> gson.fromJson(json_string, Array<Category>::class.java ).toList() as List<T>
        else -> null
    }

     return modelList
}