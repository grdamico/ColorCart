package com.grdamico.colorcart.domain.usecase

import com.grdamico.colorcart.domain.model.ReceiptRow

class GenerateBisName {
    operator fun invoke(
        proposedName: String,
        existingRows: List<ReceiptRow>
    ): String? {
        val normalized = proposedName.trim()
        if (normalized.isBlank()) return null

        val existsBase = existingRows.any { row ->
            row.item.equals(normalized, ignoreCase = true)
        }

        val bisName = "$normalized Bis"

        val existsBis = existingRows.any { row ->
            row.item.equals(bisName, ignoreCase = true)
        }

        return when {
            !existsBase -> normalized
            !existsBis -> bisName
            else -> null
        }
    }
}