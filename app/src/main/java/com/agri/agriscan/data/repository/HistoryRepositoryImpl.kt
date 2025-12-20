package com.agri.agriscan.data.repository

import com.agri.agriscan.data.local.database.dao.HistoryDao
import com.agri.agriscan.domain.model.History
import com.agri.agriscan.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override suspend fun insertHistory(history: History) {
        historyDao.insertHistory(history)
    }

    override fun getHistory(): Flow<List<History>> {
        return historyDao.getHistory()
    }

    override suspend fun clearHistory() {
        historyDao.clearHistory()
    }
}