package com.grdamico.colorcart.ui.receipt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReceiptScreen(
    viewModel: ReceiptViewModel,
    onOpenCamera: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Receipt",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(uiState.rows, key = { it.id }) { row ->
                ReceiptRowItem(
                    row = row,
                    onClick = { viewModel.startEditing(row) }
                )
            }
        }

        ColorTotalsSection(
            totals = uiState.colorTotals,
            grandTotal = uiState.grandTotal
        )

        Button(
            onClick = onOpenCamera,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Back to camera")
        }
    }

    uiState.editingRow?.let { row ->
        ReceiptEditDialog(
            row = row,
            onDismiss = { viewModel.stopEditing() },
            onSave = { newPrice, newQty, newColor ->
                viewModel.updateRow(
                    id = row.id,
                    newPrice = newPrice,
                    newQty = newQty,
                    newColor = newColor
                )
            }
        )
    }
}