package com.grdamico.colorcart.ui.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grdamico.colorcart.data.repository.ReceiptRepository
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
import kotlinx.coroutines.launch

class ReceiptViewModel(
    private val repository: ReceiptRepository
) : ViewModel() {

    private val calculateColorTotals = CalculateColorTotals()
    private val calculateGrandTotal = CalculateGrandTotal()
    private val generateBisName = GenerateBisName()

    private val _searchQuery = MutableStateFlow("")
    private val _editingRowId = MutableStateFlow<Long?>(null)

    private val rowsFlow = repository.observeRows()

    val uiState: StateFlow<ReceiptUiState> = combine(
        rowsFlow,
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
        initialValue = ReceiptUiState()
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

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun startEditing(rowId: Long) {
        _editingRowId.value = rowId
    }

    fun stopEditing() {
        _editingRowId.value = null
    }

    fun addRow(
        proposedName: String,
        price: Double,
        qty: Int,
        color: RowColor
    ): Boolean {
        val currentRows = uiState.value.rows
        val normalizedName = proposedName.trim()

        if (normalizedName.isBlank()) return false
        if (price < 0.0) return false
        if (qty <= 0) return false

        val finalName = generateBisName(
            proposedName = normalizedName,
            existingRows = currentRows
        ) ?: return false

        viewModelScope.launch {
            val newRow = ReceiptRow(
                id = repository.nextId(),
                item = finalName,
                price = price,
                qty = qty,
                color = color
            )

            repository.insertRow(newRow)
        }

        return true
    }

    fun updateRow(
        id: Long,
        newPrice: Double,
        newQty: Int,
        newColor: RowColor
    ) {
        if (newPrice < 0.0) return
        if (newQty <= 0) return

        val currentRow = uiState.value.rows.find { it.id == id } ?: return

        viewModelScope.launch {
            repository.updateRow(
                currentRow.copy(
                    price = newPrice,
                    qty = newQty,
                    color = newColor
                )
            )
            _editingRowId.value = null
        }
    }

    fun deleteRow(id: Long) {
        viewModelScope.launch {
            repository.deleteRow(id)
            if (_editingRowId.value == id) {
                _editingRowId.value = null
            }
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearAll()
            _editingRowId.value = null
        }
    }

    fun productNameExists(name: String): Boolean {
        val normalized = name.trim()
        if (normalized.isBlank()) return false

        return uiState.value.rows.any { row ->
            row.item.equals(normalized, ignoreCase = true)
        }
    }
}