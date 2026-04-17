package com.grdamico.colorcart.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.grdamico.colorcart.data.local.dao.ReceiptDao
import com.grdamico.colorcart.data.local.entity.ReceiptRowEntity

@Database(
    entities = [ReceiptRowEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ColorCartDatabase : RoomDatabase() {
    abstract fun receiptDao(): ReceiptDao
}