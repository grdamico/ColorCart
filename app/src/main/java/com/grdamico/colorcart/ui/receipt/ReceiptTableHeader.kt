package com.grdamico.colorcart.ui.receipt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReceiptTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Item",
            modifier = Modifier.weight(1.6f),
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = "Qty",
            modifier = Modifier.weight(0.5f),
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = "Price",
            modifier = Modifier.weight(0.8f),
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = "Total",
            modifier = Modifier.weight(0.8f),
            style = MaterialTheme.typography.labelMedium
        )
    }
}