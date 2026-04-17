package com.grdamico.colorcart.data.repository

import com.grdamico.colorcart.data.local.dao.ReceiptDao
import com.grdamico.colorcart.data.local.mapper.toDomain
import com.grdamico.colorcart.data.local.mapper.toEntity
import com.grdamico.colorcart.domain.model.ReceiptRow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReceiptRepository(
    private val receiptDao: ReceiptDao
) {
    fun observeRows(): Flow<List<ReceiptRow>> {
        return receiptDao.observeAllRows().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun insertRow(row: ReceiptRow) {
        receiptDao.insertRow(row.toEntity())
    }

    suspend fun updateRow(row: ReceiptRow) {
        receiptDao.updateRow(row.toEntity())
    }

    suspend fun deleteRow(id: Long) {
        receiptDao.deleteRowById(id)
    }

    suspend fun clearAll() {
        receiptDao.clearAll()
    }

    suspend fun nextId(): Long {
        return (receiptDao.getMaxId() ?: 0L) + 1L
    }
}