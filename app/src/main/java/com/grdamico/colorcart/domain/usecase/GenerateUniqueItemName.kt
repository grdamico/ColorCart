package com.grdamico.colorcart.domain.usecase

import com.grdamico.colorcart.domain.model.ReceiptRow

class GenerateBisName {
    operator fun invoke(
        proposedName: String,
        existingRows: List<ReceiptRow>
    ): String {
        val normalized = proposedName.trim()
        if (normalized.isBlank()) return proposedName

        val alreadyExists = existingRows.any { row ->
            row.item.equals(normalized, ignoreCase = true)
        }

        return if (alreadyExists) "$normalized Bis" else normalized
    }
}