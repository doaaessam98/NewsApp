package com.example.newsapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.newsapp.data.repo.IRepository
import com.example.newsapp.data.repo.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
 interface AppModule {


    @Binds
    fun provideRepository(repository: Repository): IRepository

}