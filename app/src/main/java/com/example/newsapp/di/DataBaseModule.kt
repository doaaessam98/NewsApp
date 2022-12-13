package com.example.newsapp.di

import android.content.Context
import androidx.room.Room
import com.example.newsapp.data.source.local.database.NewsDao
import com.example.newsapp.data.source.local.database.NewsDataBase
import com.example.newsapp.data.source.local.database.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {



     @Provides
     @Singleton
     fun NewsDataBase(@ApplicationContext context: Context): NewsDataBase =
         Room.databaseBuilder(context,NewsDataBase::class.java,"news_DB").build()

    @Provides
    @Singleton
    fun provideNewsDataBase(db:NewsDataBase): NewsDao =db.NewsDao()

    @Provides
    @Singleton
    fun provideRemoteKeysDataBase(db:NewsDataBase): RemoteKeysDao =db.remoteKeysDao()
}


