package com.cookiss.moviesapp.di

import com.cookiss.moviesapp.data.remote.ApiService
import com.cookiss.moviesapp.data.repository.HomeRepositoryImpl
import com.cookiss.moviesapp.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesHomeRepository(
        apiService: ApiService
    ) : HomeRepository {
        return HomeRepositoryImpl(apiService)
    }

}