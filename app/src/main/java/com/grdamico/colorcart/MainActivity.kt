package com.grdamico.colorcart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grdamico.colorcart.camera.CameraScreen
import com.grdamico.colorcart.ui.receipt.ReceiptScreen
import com.grdamico.colorcart.ui.receipt.ReceiptViewModel
import com.grdamico.colorcart.ui.theme.ColorCartTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ReceiptViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ColorCartTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "camera"
                    ) {

                        composable("camera") {
                            CameraScreen(
                                viewModel = viewModel,
                                onOpenReceipt = {
                                    navController.navigate("receipt")
                                }
                            )
                        }

                        composable("receipt") {
                            ReceiptScreen(
                                viewModel = viewModel,
                                onOpenCamera = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}