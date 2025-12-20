package com.agri.agriscan.di

import android.content.Context
import androidx.room.Room
import com.agri.agriscan.data.local.database.AppDatabase
import com.agri.agriscan.data.local.database.dao.DiseaseDao
import com.agri.agriscan.data.local.database.dao.HistoryDao
import com.agri.agriscan.data.local.database.dao.PlantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "agriscan-db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providePlantDao(appDatabase: AppDatabase): PlantDao {
        return appDatabase.plantDao()
    }

    @Provides
    @Singleton
    fun provideDiseaseDao(appDatabase: AppDatabase): DiseaseDao {
        return appDatabase.diseaseDao()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(appDatabase: AppDatabase): HistoryDao {
        return appDatabase.historyDao()
    }
}