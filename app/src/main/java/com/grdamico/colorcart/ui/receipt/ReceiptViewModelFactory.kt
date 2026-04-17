package com.grdamico.colorcart.ui.receipt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grdamico.colorcart.data.repository.ReceiptRepository

class ReceiptViewModelFactory(
    private val repository: ReceiptRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceiptViewModel::class.java)) {
            return ReceiptViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}