package com.grdamico.colorcart.ui.receipt

import com.grdamico.colorcart.domain.model.ReceiptRow
import com.grdamico.colorcart.domain.model.RowColor

data class ReceiptUiState(
    val rows: List<ReceiptRow> = emptyList(),
    val colorTotals: Map<RowColor, Double> = emptyMap(),
    val grandTotal: Double = 0.0,
    val editingRow: ReceiptRow? = null
)