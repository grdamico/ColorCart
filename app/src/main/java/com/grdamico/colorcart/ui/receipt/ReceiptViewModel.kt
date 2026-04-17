package com.grdamico.colorcart.ui.receipt

import androidx.lifecycle.ViewModel
import com.grdamico.colorcart.domain.model.ReceiptRow
import com.grdamico.colorcart.domain.model.RowColor
import com.grdamico.colorcart.domain.usecase.CalculateColorTotals
import com.grdamico.colorcart.domain.usecase.CalculateGrandTotal
import com.grdamico.colorcart.domain.usecase.GenerateBisName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ReceiptViewModel : ViewModel() {

    private val calculateColorTotals = CalculateColorTotals()
    private val calculateGrandTotal = CalculateGrandTotal()
    private val generateBisName = GenerateBisName()

    private val _rows = MutableStateFlow(
        listOf(
            ReceiptRow(1L, "Apples", 2.40, 1, RowColor.GREEN),
            ReceiptRow(2L, "Milk", 1.89, 1, RowColor.YELLOW),
            ReceiptRow(3L, "Bread", 1.50, 2, RowColor.NONE)
        )
    )

    private var currentSearchQuery: String = ""

    private val _uiState = MutableStateFlow(
        buildUiState(
            rows = _rows.value,
            editingRow = null,
            searchQuery = currentSearchQuery
        )
    )
    val uiState: StateFlow<ReceiptUiState> = _uiState

    private fun filterRows(
        rows: List<ReceiptRow>,
        query: String
    ): List<ReceiptRow> {
        if (query.isBlank()) return rows

        return rows.filter { row ->
            row.item.contains(query.trim(), ignoreCase = true)
        }
    }

    private fun buildUiState(
        rows: List<ReceiptRow>,
        editingRow: ReceiptRow?,
        searchQuery: String
    ): ReceiptUiState {
        val filteredRows = filterRows(rows, searchQuery)

        return ReceiptUiState(
            rows = rows,
            filteredRows = filteredRows,
            colorTotals = calculateColorTotals(filteredRows),
            grandTotal = calculateGrandTotal(filteredRows),
            editingRow = editingRow,
            searchQuery = searchQuery
        )
    }

    private fun refreshUiState(
        editingRow: ReceiptRow? = _uiState.value.editingRow
    ) {
        _uiState.value = buildUiState(
            rows = _rows.value,
            editingRow = editingRow,
            searchQuery = currentSearchQuery
        )
    }

    private fun nextId(): Long {
        return (_rows.value.maxOfOrNull { it.id } ?: 0L) + 1L
    }

    fun getDisplayNameForSave(proposedName: String): String {
        return generateBisName(
            proposedName = proposedName,
            existingRows = _rows.value
        )
    }

    fun addRow(
        proposedName: String,
        price: Double,
        qty: Int,
        color: RowColor
    ) {
        val finalName = getDisplayNameForSave(proposedName)

        val newRow = ReceiptRow(
            id = nextId(),
            item = finalName,
            price = price,
            qty = qty,
            color = color
        )

        _rows.update { rows ->
            rows + newRow
        }

        refreshUiState()
    }

    fun updateSearchQuery(query: String) {
        currentSearchQuery = query
        refreshUiState()
    }

    fun startEditing(row: ReceiptRow) {
        refreshUiState(editingRow = row)
    }

    fun stopEditing() {
        refreshUiState(editingRow = null)
    }

    fun updateRow(
        id: Long,
        newPrice: Double,
        newQty: Int,
        newColor: RowColor
    ) {
        _rows.update { rows ->
            rows.map { row ->
                if (row.id == id) {
                    row.copy(
                        price = newPrice,
                        qty = newQty,
                        color = newColor
                    )
                } else {
                    row
                }
            }
        }

        refreshUiState(editingRow = null)
    }

    fun productNameExists(name: String): Boolean {
        val normalized = name.trim()
        if (normalized.isBlank()) return false

        return _rows.value.any { row ->
            row.item.equals(normalized, ignoreCase = true)
        }
    }
}