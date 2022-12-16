package com.example.newsapp.di

import com.example.newsapp.data.repo.IRepository
import com.example.newsapp.data.repo.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
 interface AppModule {


    @Binds
    fun provideRepository(repository: Repository): IRepository
}