package com.grdamico.colorcart.data.local.mapper

import com.grdamico.colorcart.data.local.entity.ReceiptRowEntity
import com.grdamico.colorcart.domain.model.ReceiptRow
import com.grdamico.colorcart.domain.model.RowColor

fun ReceiptRowEntity.toDomain(): ReceiptRow {
    return ReceiptRow(
        id = id,
        item = item,
        price = price,
        qty = qty,
        color = runCatching { RowColor.valueOf(color) }.getOrDefault(RowColor.NONE)
    )
}

fun ReceiptRow.toEntity(): ReceiptRowEntity {
    return ReceiptRowEntity(
        id = id,
        item = item,
        price = price,
        qty = qty,
        color = color.name
    )
}