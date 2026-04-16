package com.grdamico.colorcart.domain.usecase

import com.grdamico.colorcart.domain.model.ReceiptRow
import com.grdamico.colorcart.domain.model.RowColor

class CalculateColorTotals {
    operator fun invoke(rows: List<ReceiptRow>): Map<RowColor, Double> {
        return RowColor.entries.associateWith { color ->
            rows
                .filter { it.color == color }
                .sumOf { it.lineTotal }
        }
    }
}