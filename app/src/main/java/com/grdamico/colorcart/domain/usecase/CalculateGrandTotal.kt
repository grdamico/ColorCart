package com.grdamico.colorcart.domain.usecase

import com.grdamico.colorcart.domain.model.ReceiptRow

class CalculateGrandTotal {
    operator fun invoke(rows: List<ReceiptRow>): Double {
        return rows.sumOf { it.lineTotal }
    }
}