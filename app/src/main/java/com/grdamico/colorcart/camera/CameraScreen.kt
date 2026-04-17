package com.grdamico.colorcart.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.grdamico.colorcart.ui.components.SaveProductDialog
import com.grdamico.colorcart.ui.receipt.ReceiptViewModel

@Composable
fun CameraScreen(
    viewModel: ReceiptViewModel,
    onOpenReceipt: () -> Unit
) {
    var showSaveDialog by remember { mutableStateOf(false) }
    var duplicateBisMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Camera screen")

        Button(onClick = { showSaveDialog = true }) {
            Text("Take photo")
        }

        Button(onClick = onOpenReceipt) {
            Text("Open receipt")
        }
    }

    if (showSaveDialog) {
        SaveProductDialog(
            initialName = "Milk",
            initialPrice = "2.49",
            initialQuantity = "1",
            title = "Save product",
            onDismiss = { showSaveDialog = false },
            onSave = { name, price, qty, color ->
                val added = viewModel.addRow(
                    proposedName = name,
                    price = price,
                    qty = qty,
                    color = color
                )

                if (added) {
                    showSaveDialog = false
                } else {
                    showSaveDialog = false
                    duplicateBisMessage =
                        "You cannot add ${name.trim()} Bis because a Bis of ${name.trim()} already exists."
                }
            }
        )
    }

    duplicateBisMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { duplicateBisMessage = null },
            title = { Text("Cannot add product") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { duplicateBisMessage = null }) {
                    Text("OK")
                }
            }
        )
    }
}