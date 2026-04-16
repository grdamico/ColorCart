package com.grdamico.colorcart.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.grdamico.colorcart.ui.receipt.ReceiptViewModel

@Composable
fun CameraScreen(
    viewModel: ReceiptViewModel,
    onOpenReceipt: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Camera screen")
        Button(onClick = onOpenReceipt) {
            Text("Open receipt")
        }
    }
}