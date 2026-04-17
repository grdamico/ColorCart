package com.grdamico.colorcart.domain.model

data class ReceiptRow(
    val id: Long,
    val item: String,
    val price: Double,
    val qty: Int,
    val color: RowColor
)