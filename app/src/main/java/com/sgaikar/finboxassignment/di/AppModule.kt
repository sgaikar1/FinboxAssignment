package com.sgaikar.finboxassignment.di

import android.content.Context
import com.sgaikar.finboxassignment.data.local.AppDatabase
import com.sgaikar.finboxassignment.data.local.LocationDao
import com.sgaikar.finboxassignment.data.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideLocationDao(db: AppDatabase) = db.locationDao()

    @Singleton
    @Provides
    fun provideRepository(localDataSource: LocationDao) =
        LocationRepository(localDataSource)
}