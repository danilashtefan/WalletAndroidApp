package com.example.wallet

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
open class NetworkModule {
    open fun getBaseUrl () ="https://api.spacexdata.com/v3/"

    @Provides
    fun provideBaseUrl() = getBaseUrl ()

    // The rest of Builders, Factories, etc. are omitted for simplicity
}