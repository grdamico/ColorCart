package com.grdamico.colorcart.ocr

import android.graphics.Rect
import com.google.mlkit.vision.text.Text
import kotlin.math.abs

object ProductLabelParser {

    private val priceRegex = Regex("""(?<!\d)(\d{1,3}[.,]\d{2})(?!\d)""")

    private val blacklistWords = listOf(
        "OFFERTA",
        "RISPARMI",
        "COUPON",
        "PLUS",
        "PUNTI",
        "ORIGINE",
        "PREZZO NORMALE",
        "PREZZO SCONTATO",
        "LIDL",
        "PESO",
        "CALIBRO",
        "CAT",
        "€/KG",
        "€/ KG",
        "EURO/KG",
        "%"
    )

    fun parse(text: Text): LabelParseResult {
        val lines = text.textBlocks
            .flatMap { block -> block.lines }
            .mapIndexed { index, line ->
                ParsedLine(
                    index = index,
                    text = normalizeSpaces(line.text),
                    boundingBox = line.boundingBox
                )
            }
            .filter { it.text.isNotBlank() }

        val priceCandidate = findBestPrice(lines)
        val nameCandidate = findBestName(
            lines = lines,
            priceBox = priceCandidate?.boundingBox
        )

        return LabelParseResult(
            productName = nameCandidate?.text.orEmpty(),
            price = priceCandidate?.value.orEmpty(),
            rawText = text.text
        )
    }

    private fun findBestPrice(lines: List<ParsedLine>): PriceCandidate? {
        val candidates = mutableListOf<PriceCandidate>()

        lines.forEach { line ->
            val matches = priceRegex.findAll(line.text).toList()
            matches.forEach { match ->
                val value = match.groupValues[1].replace(',', '.')
                val score = scorePriceCandidate(
                    lineText = line.text,
                    boundingBox = line.boundingBox
                )

                candidates += PriceCandidate(
                    value = value,
                    score = score,
                    boundingBox = line.boundingBox,
                    sourceText = line.text
                )
            }
        }

        return candidates.maxByOrNull { it.score }
    }

    private fun scorePriceCandidate(
        lineText: String,
        boundingBox: Rect?
    ): Int {
        var score = 0

        val upper = lineText.uppercase()

        val height = boundingBox?.height() ?: 0
        score += height * 3

        if (upper.contains("RISPARMI")) score -= 200
        if (upper.contains("NORMALE")) score -= 180
        if (upper.contains("%")) score -= 150
        if (upper.contains("COUPON")) score -= 100

        if (upper.contains("PREZZO") && !upper.contains("NORMALE")) score += 20
        if (upper.contains("€")) score += 10

        return score
    }

    private fun findBestName(
        lines: List<ParsedLine>,
        priceBox: Rect?
    ): ParsedLine? {
        val candidates = lines.mapNotNull { line ->
            val score = scoreNameCandidate(line, priceBox)
            if (score == null) null else line to score
        }

        return candidates.maxByOrNull { it.second }?.first
    }

    private fun scoreNameCandidate(
        line: ParsedLine,
        priceBox: Rect?
    ): Int? {
        val text = line.text.trim()
        val upper = text.uppercase()

        if (text.isBlank()) return null
        if (!text.any { it.isLetter() }) return null
        if (priceRegex.containsMatchIn(text)) return null
        if (text.length < 3) return null
        if (blacklistWords.any { upper.contains(it) }) return null

        var score = 0

        val letters = text.count { it.isLetter() }
        val spaces = text.count { it.isWhitespace() }

        score += letters * 2
        score += spaces * 3

        if (!text.any { it.isDigit() }) score += 20
        if (text.length in 8..40) score += 20

        val box = line.boundingBox
        if (priceBox != null && box != null) {
            val verticalDistance = abs(box.centerY() - priceBox.centerY())
            score += maxOf(0, 120 - verticalDistance)
        }

        return score
    }

    private fun normalizeSpaces(value: String): String {
        return value.replace("\\s+".toRegex(), " ").trim()
    }

    private data class ParsedLine(
        val index: Int,
        val text: String,
        val boundingBox: Rect?
    )

    private data class PriceCandidate(
        val value: String,
        val score: Int,
        val boundingBox: Rect?,
        val sourceText: String
    )
}