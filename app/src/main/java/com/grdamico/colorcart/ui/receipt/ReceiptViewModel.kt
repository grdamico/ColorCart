package com.grdamico.colorcart.ui.receipt

import androidx.lifecycle.ViewModel
import com.grdamico.colorcart.domain.model.ReceiptRow
import com.grdamico.colorcart.domain.model.RowColor
import com.grdamico.colorcart.domain.usecase.CalculateColorTotals
import com.grdamico.colorcart.domain.usecase.CalculateGrandTotal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ReceiptViewModel : ViewModel() {

    private val calculateColorTotals = CalculateColorTotals()
    private val calculateGrandTotal = CalculateGrandTotal()

    private val _rows = MutableStateFlow(
        listOf(
            ReceiptRow(1L, "Apples", 2.40, 1, RowColor.GREEN),
            ReceiptRow(2L, "Milk", 1.89, 1, RowColor.YELLOW),
            ReceiptRow(3L, "Bread", 1.50, 2, RowColor.NONE)
        )
    )

    private val _uiState = MutableStateFlow(buildUiState(_rows.value))
    val uiState: StateFlow<ReceiptUiState> = _uiState

    private fun buildUiState(rows: List<ReceiptRow>, editingRow: ReceiptRow? = null): ReceiptUiState {
        return ReceiptUiState(
            rows = rows,
            colorTotals = calculateColorTotals(rows),
            grandTotal = calculateGrandTotal(rows),
            editingRow = editingRow
        )
    }

    private fun refreshUiState(editingRow: ReceiptRow? = _uiState.value.editingRow) {
        _uiState.value = buildUiState(_rows.value, editingRow)
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
}