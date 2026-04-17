package com.grdamico.colorcart.ui.receipt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grdamico.colorcart.ui.components.SaveProductDialog

@Composable
fun ReceiptScreen(
    viewModel: ReceiptViewModel,
    onOpenCamera: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Receipt",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::updateSearchQuery,
                label = { Text("Search product") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { showAddDialog = true }
            ) {
                Text("+")
            }
        }

        ReceiptTableHeader()

        if (uiState.filteredRows.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (uiState.searchQuery.isBlank()) {
                        "No products yet"
                    } else {
                        "No products found"
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = uiState.filteredRows,
                    key = { it.id }
                ) { row ->
                    ReceiptRowItem(
                        row = row,
                        onClick = { viewModel.startEditing(row.id) }
                    )
                }
            }
        }

        ColorTotalsSection(
            totals = uiState.colorTotals,
            grandTotal = uiState.grandTotal,
            onBackToCamera = onOpenCamera
        )
    }

    if (showAddDialog) {
        SaveProductDialog(
            initialName = "",
            initialPrice = "",
            initialQuantity = "1",
            title = "Add product",
            onDismiss = { showAddDialog = false },
            onSave = { name, price, qty, color ->
                viewModel.addRow(
                    proposedName = name,
                    price = price,
                    qty = qty,
                    color = color
                )
                showAddDialog = false
            }
        )
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