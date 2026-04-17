package com.grdamico.colorcart.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.grdamico.colorcart.data.local.entity.ReceiptRowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {

    @Query("SELECT * FROM receipt_rows ORDER BY id ASC")
    fun observeAllRows(): Flow<List<ReceiptRowEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRow(row: ReceiptRowEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRows(rows: List<ReceiptRowEntity>)

    @Update
    suspend fun updateRow(row: ReceiptRowEntity)

    @Query("DELETE FROM receipt_rows WHERE id = :id")
    suspend fun deleteRowById(id: Long)

    @Query("SELECT MAX(id) FROM receipt_rows")
    suspend fun getMaxId(): Long?

    @Query("DELETE FROM receipt_rows")
    suspend fun clearAll()
}