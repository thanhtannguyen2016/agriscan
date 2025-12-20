package com.agri.agriscan.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.agri.agriscan.data.local.database.dao.DiseaseDao
import com.agri.agriscan.data.local.database.dao.HistoryDao
import com.agri.agriscan.data.local.database.dao.PlantDao
import com.agri.agriscan.domain.model.Disease
import com.agri.agriscan.domain.model.History
import com.agri.agriscan.domain.model.Plant

@Database(entities = [Plant::class, Disease::class, History::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
    abstract fun diseaseDao(): DiseaseDao
    abstract fun historyDao(): HistoryDao
}