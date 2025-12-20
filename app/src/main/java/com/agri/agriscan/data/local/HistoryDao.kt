package com.agri.agriscan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.agri.agriscan.domain.model.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getHistory(): Flow<List<History>>

    @Query("DELETE FROM history")
    suspend fun clearHistory()
}