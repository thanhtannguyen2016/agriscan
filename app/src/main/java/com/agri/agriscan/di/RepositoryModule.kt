package com.agri.agriscan.di

import com.agri.agriscan.data.repository.DiseaseRepositoryImpl
import com.agri.agriscan.data.repository.HistoryRepositoryImpl
import com.agri.agriscan.data.repository.PlantRepositoryImpl
import com.agri.agriscan.data.repository.TreatmentRepositoryImpl
import com.agri.agriscan.domain.repository.DiseaseRepository
import com.agri.agriscan.domain.repository.HistoryRepository
import com.agri.agriscan.domain.repository.PlantRepository
import com.agri.agriscan.domain.repository.TreatmentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPlantRepository(
        plantRepositoryImpl: PlantRepositoryImpl
    ): PlantRepository

    @Binds
    abstract fun bindDiseaseRepository(
        diseaseRepositoryImpl: DiseaseRepositoryImpl
    ): DiseaseRepository

    @Binds
    abstract fun bindHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository

    @Binds
    abstract fun bindTreatmentRepository(
        treatmentRepositoryImpl: TreatmentRepositoryImpl
    ): TreatmentRepository
}