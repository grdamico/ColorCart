package com.grdamico.colorcart.ui.receipt

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.grdamico.colorcart.domain.model.ReceiptRow
import com.grdamico.colorcart.domain.model.RowColor

class ReceiptViewModel : ViewModel() {

    private val _rows = MutableStateFlow(
        listOf(
            ReceiptRow(
                id = 1L,
                item = "Apples",
                price = 2.40,
                qty = 1,
                color = RowColor.GREEN
            ),
            ReceiptRow(
                id = 2L,
                item = "Milk",
                price = 1.89,
                qty = 1,
                color = RowColor.YELLOW
            ),
            ReceiptRow(
                id = 3L,
                item = "Bread",
                price = 1.50,
                qty = 2,
                color = RowColor.NONE
            )
        )
    )

    val rows: StateFlow<List<ReceiptRow>> = _rows
}