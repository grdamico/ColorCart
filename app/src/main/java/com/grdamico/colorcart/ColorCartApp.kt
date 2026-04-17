package com.grdamico.colorcart

import android.app.Application
import androidx.room.Room
import com.grdamico.colorcart.data.local.db.ColorCartDatabase
import com.grdamico.colorcart.data.repository.ReceiptRepository

class ColorCartApp : Application() {

    lateinit var database: ColorCartDatabase
        private set

    lateinit var receiptRepository: ReceiptRepository
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            ColorCartDatabase::class.java,
            "colorcart.db"
        ).build()

        receiptRepository = ReceiptRepository(database.receiptDao())
    }
}