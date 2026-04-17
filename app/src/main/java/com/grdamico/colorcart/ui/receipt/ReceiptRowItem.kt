package com.grdamico.colorcart.ui.receipt

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.grdamico.colorcart.domain.model.ReceiptRow
import com.grdamico.colorcart.domain.model.RowColor
import com.grdamico.colorcart.ui.theme.ReceiptColors

@Composable
fun ReceiptRowItem(
    row: ReceiptRow,
    onClick: () -> Unit
) {
    val lineTotal = row.price * row.qty

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = rowBackgroundColor(row.color),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = row.item,
                modifier = Modifier.weight(1.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = row.qty.toString(),
                modifier = Modifier.weight(0.5f),
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "%.2f".format(row.price),
                modifier = Modifier.weight(0.8f),
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "%.2f".format(lineTotal),
                modifier = Modifier.weight(0.8f),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun rowBackgroundColor(color: RowColor): Color {
    return when (color) {
        RowColor.NONE -> ReceiptColors.None
        RowColor.GREEN -> ReceiptColors.Green
        RowColor.YELLOW -> ReceiptColors.Yellow
        RowColor.BLUE -> ReceiptColors.Blue
        RowColor.PINK -> ReceiptColors.Pink
    }
}