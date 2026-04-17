package com.grdamico.colorcart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipt_rows")
data class ReceiptRowEntity(
    @PrimaryKey
    val id: Long,
    val item: String,
    val price: Double,
    val qty: Int,
    val color: String
)