package com.agri.agriscan.di

import com.agri.agriscan.data.repository.DiseaseRepositoryImpl
import com.agri.agriscan.data.repository.PlantRepositoryImpl
import com.agri.agriscan.data.repository.TreatmentRepositoryImpl
import com.agri.agriscan.domain.repository.DiseaseRepository
import com.agri.agriscan.domain.repository.PlantRepository
import com.agri.agriscan.domain.repository.TreatmentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPlantRepository(
        plantRepositoryImpl: PlantRepositoryImpl
    ): PlantRepository

    @Binds
    @Singleton
    abstract fun bindDiseaseRepository(
        diseaseRepositoryImpl: DiseaseRepositoryImpl
    ): DiseaseRepository

    @Binds
    @Singleton
    abstract fun bindTreatmentRepository(
        treatmentRepositoryImpl: TreatmentRepositoryImpl
    ): TreatmentRepository
}