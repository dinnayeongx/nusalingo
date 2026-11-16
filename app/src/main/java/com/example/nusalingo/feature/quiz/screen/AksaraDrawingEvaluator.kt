package com.example.nusalingo.feature.quiz.screen

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import androidx.compose.ui.geometry.Offset
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

internal object AksaraDrawingEvaluator {
    private const val MAX_EVALUATION_DIMENSION = 320

    fun isSimilar(
        strokes: List<List<Offset>>,
        character: String,
        width: Int,
        height: Int,
        strokeWidthPx: Float
    ): Boolean {
        if (strokes.isEmpty() || width <= 0 || height <= 0 || strokeWidthPx <= 0f) {
            return false
        }

        val scale = min(
            1f,
            MAX_EVALUATION_DIMENSION / max(width, height).toFloat()
        )
        val evaluationWidth = (width * scale).roundToInt().coerceAtLeast(1)
        val evaluationHeight = (height * scale).roundToInt().coerceAtLeast(1)
        val evaluationStrokeWidth = strokeWidthPx * scale

        val guideBitmap = Bitmap.createBitmap(
            evaluationWidth,
            evaluationHeight,
            Bitmap.Config.ARGB_8888
        )
        val drawingBitmap = Bitmap.createBitmap(
            evaluationWidth,
            evaluationHeight,
            Bitmap.Config.ARGB_8888
        )
        try {
            val guidePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.BLACK
                textSize = evaluationHeight * 0.65f
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT
            }
            val fontMetrics = guidePaint.fontMetrics
            val baseline =
                evaluationHeight / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f
            android.graphics.Canvas(guideBitmap).drawText(
                character,
                evaluationWidth / 2f,
                baseline,
                guidePaint
            )

            val drawingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.BLACK
                style = Paint.Style.STROKE
                strokeWidth = evaluationStrokeWidth
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
            }
            val drawingCanvas = android.graphics.Canvas(drawingBitmap)
            strokes.forEach { stroke ->
                when (stroke.size) {
                    0 -> Unit
                    1 -> drawingCanvas.drawCircle(
                        stroke.first().x * scale,
                        stroke.first().y * scale,
                        evaluationStrokeWidth / 2f,
                        drawingPaint
                    )
                    else -> {
                        val path = Path().apply {
                            moveTo(stroke.first().x * scale, stroke.first().y * scale)
                            stroke.drop(1).forEach { point ->
                                lineTo(point.x * scale, point.y * scale)
                            }
                        }
                        drawingCanvas.drawPath(path, drawingPaint)
                    }
                }
            }

            val guideMask = createMask(guideBitmap)
            val drawingMask = createMask(drawingBitmap)
            val guideCount = guideMask.count { it }
            val drawingCount = drawingMask.count { it }
            if (guideCount == 0 || drawingCount == 0) return false

            val distanceToGuide = distanceTransform(
                guideMask,
                evaluationWidth,
                evaluationHeight
            )
            val distanceToDrawing = distanceTransform(
                drawingMask,
                evaluationWidth,
                evaluationHeight
            )
            val tolerance = maxOf(
                evaluationStrokeWidth * 1.5f,
                min(evaluationWidth, evaluationHeight) * 0.035f
            )

            val guideCoverage = guideMask.indices.count { index ->
                guideMask[index] && distanceToDrawing[index] <= tolerance
            }.toFloat() / guideCount
            val drawingCoverage = drawingMask.indices.count { index ->
                drawingMask[index] && distanceToGuide[index] <= tolerance
            }.toFloat() / drawingCount

            return guideCoverage >= 0.45f && drawingCoverage >= 0.65f
        } finally {
            guideBitmap.recycle()
            drawingBitmap.recycle()
        }
    }

    private fun createMask(bitmap: Bitmap): BooleanArray {
        val pixels = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return BooleanArray(pixels.size) { index ->
            android.graphics.Color.alpha(pixels[index]) >= 48
        }
    }

    private fun distanceTransform(mask: BooleanArray, width: Int, height: Int): FloatArray {
        val diagonalDistance = sqrt(2f)
        val infinity = (width + height).toFloat()
        val distances = FloatArray(mask.size) { index -> if (mask[index]) 0f else infinity }

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = y * width + x
                var distance = distances[index]
                if (x > 0) distance = minOf(distance, distances[index - 1] + 1f)
                if (y > 0) distance = minOf(distance, distances[index - width] + 1f)
                if (x > 0 && y > 0) {
                    distance = minOf(distance, distances[index - width - 1] + diagonalDistance)
                }
                if (x < width - 1 && y > 0) {
                    distance = minOf(distance, distances[index - width + 1] + diagonalDistance)
                }
                distances[index] = distance
            }
        }

        for (y in height - 1 downTo 0) {
            for (x in width - 1 downTo 0) {
                val index = y * width + x
                var distance = distances[index]
                if (x < width - 1) distance = minOf(distance, distances[index + 1] + 1f)
                if (y < height - 1) distance = minOf(distance, distances[index + width] + 1f)
                if (x < width - 1 && y < height - 1) {
                    distance = minOf(distance, distances[index + width + 1] + diagonalDistance)
                }
                if (x > 0 && y < height - 1) {
                    distance = minOf(distance, distances[index + width - 1] + diagonalDistance)
                }
                distances[index] = distance
            }
        }
        return distances
    }
}
