package com.grdamico.colorcart.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.unit.dp
import com.grdamico.colorcart.domain.model.RowColor
import com.grdamico.colorcart.ui.receipt.ReceiptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveProductDialog(
    viewModel: ReceiptViewModel,
    initialName: String = "",
    initialPrice: String = "",
    initialQuantity: String = "1",
    title: String = "Add product",
    onDismiss: () -> Unit,
    onSaved: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var priceText by remember { mutableStateOf(initialPrice) }
    var qtyText by remember{ mutableStateOf(initialQuantity) }
    var selectedColor by remember { mutableStateOf(RowColor.NONE) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(initialName) {
        if (initialName.isNotBlank()) {
            name = viewModel.getDisplayNameForSave(initialName)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Price") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = qtyText,
                    onValueChange = { qtyText = it },
                    label = { Text("Quantity") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedColor.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Color") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        RowColor.entries.forEach { color ->
                            DropdownMenuItem(
                                text = { Text(color.name) },
                                onClick = {
                                    selectedColor = color
                                    expanded = false
                                }
                            )
                        }
                    }
                }


            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedPrice = priceText.toDoubleOrNull() ?: 0.0
                    val parsedQty = qtyText.toIntOrNull() ?: 0

                    viewModel.addRow(
                        proposedName = name,
                        price = parsedPrice,
                        qty = parsedQty,
                        color = selectedColor
                    )

                    onSaved()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}