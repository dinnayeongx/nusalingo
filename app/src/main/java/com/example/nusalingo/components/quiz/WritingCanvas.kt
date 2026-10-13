package com.example.nusalingo.components.quiz

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

@Composable
fun WritingCanvas(
    modifier: Modifier = Modifier,
    strokes: SnapshotStateList<List<Offset>>,
    onStrokeAdded: (List<Offset>) -> Unit,
    guideCharacter: String? = null
) {
    val currentStroke = remember {
        mutableStateListOf<Offset>()
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFFFFCF6))
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(strokes) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentStroke.clear()
                            currentStroke.add(offset)
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            currentStroke.add(change.position)
                        },
                        onDragEnd = {
                            if (currentStroke.isNotEmpty()) {
                                onStrokeAdded(currentStroke.toList())
                                currentStroke.clear()
                            }
                        },
                        onDragCancel = {
                            currentStroke.clear()
                        }
                    )
                }
        ) {
            val gridColor = Color(0xFFECE3D5)
            val cellWidth = size.width / 4
            val cellHeight = size.height / 4

            for (i in 1..3) {
                drawLine(
                    color = gridColor,
                    start = Offset(cellWidth * i, 0f),
                    end = Offset(cellWidth * i, size.height),
                    strokeWidth = 1.dp.toPx()
                )

                drawLine(
                    color = gridColor,
                    start = Offset(0f, cellHeight * i),
                    end = Offset(size.width, cellHeight * i),
                    strokeWidth = 1.dp.toPx()
                )
            }

            if (!guideCharacter.isNullOrBlank()) {
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.rgb(27, 54, 93)
                        alpha = 35
                        textSize = size.height * 0.65f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                        typeface = android.graphics.Typeface.DEFAULT
                    }

                    val fontMetrics = paint.fontMetrics
                    val centerY =
                        size.height / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f

                    drawText(
                        guideCharacter,
                        size.width / 2f,
                        centerY,
                        paint
                    )
                }
            }

            fun drawStroke(points: List<Offset>) {
                if (points.isEmpty()) return

                if (points.size == 1) {
                    drawCircle(
                        color = Color(0xFF1B365D),
                        radius = 2.5.dp.toPx(),
                        center = points.first()
                    )
                } else {
                    val path = Path().apply {
                        moveTo(points.first().x, points.first().y)

                        points.drop(1).forEach { point ->
                            lineTo(point.x, point.y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = Color(0xFF1B365D),
                        style = Stroke(
                            width = 10.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round,
                            join = androidx.compose.ui.graphics.StrokeJoin.Round
                        )
                    )
                }
            }

            strokes.forEach { stroke ->
                drawStroke(stroke)
            }

            drawStroke(currentStroke)
        }
    }
}