package com.grdamico.colorcart.ui.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grdamico.colorcart.domain.model.ReceiptRow
import com.grdamico.colorcart.domain.model.RowColor
import com.grdamico.colorcart.domain.usecase.CalculateColorTotals
import com.grdamico.colorcart.domain.usecase.CalculateGrandTotal
import com.grdamico.colorcart.domain.usecase.GenerateBisName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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

    private val _searchQuery = MutableStateFlow("")
    private val _editingRowId = MutableStateFlow<Long?>(null)

    val uiState: StateFlow<ReceiptUiState> = combine(
        _rows,
        _searchQuery,
        _editingRowId
    ) { rows, searchQuery, editingRowId ->

        val filteredRows = filterRows(rows, searchQuery)
        val editingRow = rows.find { it.id == editingRowId }

        ReceiptUiState(
            rows = rows,
            filteredRows = filteredRows,
            colorTotals = calculateColorTotals(filteredRows),
            grandTotal = calculateGrandTotal(filteredRows),
            editingRow = editingRow,
            searchQuery = searchQuery
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ReceiptUiState(
            rows = _rows.value,
            filteredRows = _rows.value,
            colorTotals = calculateColorTotals(_rows.value),
            grandTotal = calculateGrandTotal(_rows.value),
            editingRow = null,
            searchQuery = ""
        )
    )

    private fun filterRows(
        rows: List<ReceiptRow>,
        query: String
    ): List<ReceiptRow> {
        val normalizedQuery = query.trim()

        if (normalizedQuery.isBlank()) return rows

        return rows.filter { row ->
            row.item.contains(normalizedQuery, ignoreCase = true)
        }
    }

    private fun nextId(): Long {
        return (_rows.value.maxOfOrNull { it.id } ?: 0L) + 1L
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun startEditing(rowId: Long) {
        _editingRowId.value = rowId
    }

    fun stopEditing() {
        _editingRowId.value = null
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
        val normalizedName = proposedName.trim()

        if (normalizedName.isBlank()) return
        if (price < 0.0) return
        if (qty <= 0) return

        val finalName = getDisplayNameForSave(normalizedName)

        val newRow = ReceiptRow(
            id = nextId(),
            item = finalName,
            price = price,
            qty = qty,
            color = color
        )

        _rows.update { rows -> rows + newRow }
    }

    fun updateRow(
        id: Long,
        newPrice: Double,
        newQty: Int,
        newColor: RowColor
    ) {
        if (newPrice < 0.0) return
        if (newQty <= 0) return

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

        _editingRowId.value = null
    }

    fun deleteRow(id: Long) {
        _rows.update { rows ->
            rows.filterNot { it.id == id }
        }

        if (_editingRowId.value == id) {
            _editingRowId.value = null
        }
    }

    fun productNameExists(name: String): Boolean {
        val normalized = name.trim()
        if (normalized.isBlank()) return false

        return _rows.value.any { row ->
            row.item.equals(normalized, ignoreCase = true)
        }
    }
}