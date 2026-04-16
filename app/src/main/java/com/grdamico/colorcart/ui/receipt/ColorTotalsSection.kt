package com.grdamico.colorcart.ui.receipt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grdamico.colorcart.domain.model.RowColor

@Composable
fun ColorTotalsSection(
    totals: Map<RowColor, Double>,
    grandTotal: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Totals",
                style = MaterialTheme.typography.titleMedium
            )

            totals.forEach { (color, total) ->
                Text("$color: ${"%.2f".format(total)}")
            }

            Text(
                text = "Grand total: ${"%.2f".format(grandTotal)}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}