package com.grdamico.colorcart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.grdamico.colorcart.ui.receipt.ReceiptScreen
import com.grdamico.colorcart.ui.receipt.ReceiptViewModel
import com.grdamico.colorcart.ui.receipt.ReceiptViewModelFactory
import com.grdamico.colorcart.ui.theme.ColorCartTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ReceiptViewModel by viewModels {
        ReceiptViewModelFactory(
            (application as ColorCartApp).receiptRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ColorCartTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ReceiptScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}