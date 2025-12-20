package com.agri.agriscan.domain.repository

import com.agri.agriscan.domain.model.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    suspend fun insertHistory(history: History)

    fun getHistory(): Flow<List<History>>

    suspend fun clearHistory()
}