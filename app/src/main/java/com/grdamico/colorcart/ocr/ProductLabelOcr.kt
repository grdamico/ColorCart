package com.grdamico.colorcart.ocr

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.google.mlkit.vision.text.TextRecognition

object ProductLabelOcr {

    fun recognizeFromBitmap(
        bitmap: Bitmap,
        onSuccess: (LabelParseResult) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { result ->
                onSuccess(ProductLabelParser.parse(result))
            }
            .addOnFailureListener { error ->
                onError(error)
            }
    }
}