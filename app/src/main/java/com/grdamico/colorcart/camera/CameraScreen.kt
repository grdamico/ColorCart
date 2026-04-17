package com.grdamico.colorcart.camera

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.grdamico.colorcart.ocr.ProductLabelOcr
import com.grdamico.colorcart.ui.components.SaveProductDialog
import com.grdamico.colorcart.ui.receipt.ReceiptViewModel

@Composable
fun CameraScreen(
    viewModel: ReceiptViewModel,
    onOpenReceipt: () -> Unit
) {
    var showSaveDialog by remember { mutableStateOf(false) }
    var duplicateBisMessage by remember { mutableStateOf<String?>(null) }
    var ocrErrorMessage by remember { mutableStateOf<String?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    var extractedName by remember { mutableStateOf("") }
    var extractedPrice by remember { mutableStateOf("") }

    val takePicturePreviewLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap == null) {
            isProcessing = false
            return@rememberLauncherForActivityResult
        }

        ProductLabelOcr.recognizeFromBitmap(
            bitmap = bitmap,
            onSuccess = { parsed ->
                extractedName = parsed.productName
                extractedPrice = parsed.price
                isProcessing = false
                showSaveDialog = true
            },
            onError = {
                isProcessing = false
                ocrErrorMessage = "I couldn't read the label. Try again with a closer photo."
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Camera screen")

        Button(
            onClick = {
                isProcessing = true
                takePicturePreviewLauncher.launch(null)
            },
            enabled = !isProcessing
        ) {
            Text(if (isProcessing) "Reading label..." else "Take photo")
        }

        Button(onClick = onOpenReceipt) {
            Text("Open receipt")
        }
    }

    if (showSaveDialog) {
        SaveProductDialog(
            initialName = extractedName,
            initialPrice = extractedPrice,
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
                    extractedName = ""
                    extractedPrice = ""
                } else {
                    showSaveDialog = false
                    duplicateBisMessage =
                        "You already have both ${name.trim()} and ${name.trim()} Bis. Start a new receipt or edit the existing item instead."
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

    ocrErrorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { ocrErrorMessage = null },
            title = { Text("Label not read") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { ocrErrorMessage = null }) {
                    Text("OK")
                }
            }
        )
    }
}