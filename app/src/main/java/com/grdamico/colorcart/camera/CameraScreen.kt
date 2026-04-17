package com.grdamico.colorcart.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
                viewModel.addRow(
                    proposedName = name,
                    price = price,
                    qty = qty,
                    color = color
                )
                showSaveDialog = false
            }
        )
    }
}