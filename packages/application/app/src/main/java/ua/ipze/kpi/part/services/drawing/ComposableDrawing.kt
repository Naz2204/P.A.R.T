package ua.ipze.kpi.part.services.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import ua.ipze.kpi.part.services.drawing.view.IDrawingViewModel

@Composable
fun IDrawingViewModel.rememberImage(): ImageBitmap {
    val changed by this.__INTERNAL_bitmapVersion.collectAsState()
    return remember(changed) { this.__INTERNAL_getCachedBitmapImage() }
}

private data class ScreenToBitmapResult(
    val lastValidOffset: IntOffset,
    val isOvershot: Boolean
)

private fun screenToBitmap(
    screenPos: Offset,
    canvasOffset: Offset,
    scaling: Float,
    image: ImageBitmap
): ScreenToBitmapResult {
    val xReal = ((screenPos.x - canvasOffset.x) / scaling).toInt()
    val yReal = ((screenPos.y - canvasOffset.y) / scaling).toInt()

    val xClipped = xReal.coerceIn(0, image.width - 1)
    val yClipped = yReal.coerceIn(0, image.width - 1)

    return ScreenToBitmapResult(
        lastValidOffset = IntOffset(
            xClipped,
            yClipped
        ), isOvershot = xClipped != xReal || yClipped != yReal
    )
}

@Composable
fun DrawCanvas(
    modifier: Modifier = Modifier,
    view: IDrawingViewModel,
    handleDrawLine: (start: Offset, end: Offset) -> Unit
) {

    val image = view.rememberImage()
    val scaling = remember { mutableFloatStateOf(1f) }
    val lastBitmapPos = remember { mutableStateOf(Offset.Zero) }
    var wasCentered by remember { mutableStateOf(false) }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                drawAndPanPointerInput(lastBitmapPos, scaling, image, handleDrawLine)
            }
            .onSizeChanged {
                if (wasCentered) return@onSizeChanged
                wasCentered = true

                val scaleX = it.width.toFloat() / image.width
                val scaleY = it.height.toFloat() / image.height
                scaling.floatValue = minOf(scaleX, scaleY)

                val scaledWidth = image.width * scaling.floatValue
                val scaledHeight = image.height * scaling.floatValue
                lastBitmapPos.value = Offset(
                    x = (it.width - scaledWidth) / 2f,
                    y = (it.height - scaledHeight) / 2f
                )
            }
    ) {
        drawImage(
            image = image,
            srcOffset = IntOffset.Zero,
            srcSize = IntSize(
                image.width,
                image.height
            ),
            dstOffset = IntOffset(lastBitmapPos.value.x.toInt(), lastBitmapPos.value.y.toInt()),
            dstSize = IntSize(
                (image.width * scaling.floatValue).toInt(),
                (image.height * scaling.floatValue).toInt()
            )
        )
    }
}

suspend fun PointerInputScope.drawAndPanPointerInput(
    canvasOffset: MutableState<Offset>,
    scaling: MutableState<Float>,
    image: ImageBitmap,
    handleDrawLine: (start: Offset, end: Offset) -> Unit
) {
    awaitPointerEventScope {
        var isDrawing = false
        var lastBitmapPos: Offset? = null

        while (true) {
            val event = awaitPointerEvent()
            val pointers = event.changes.filter { it.pressed }

            when (pointers.size) {
                1 -> { // Single-finger drawing
                    val change = pointers.first()
                    when (event.type) {
                        PointerEventType.Press -> {
                            change.consume()
                            val bitmapPos = screenToBitmap(
                                change.position,
                                canvasOffset.value,
                                scaling.value,
                                image
                            )
                            isDrawing = !bitmapPos.isOvershot
                            lastBitmapPos = bitmapPos.lastValidOffset.toOffset()

                            if (isDrawing) {
                                handleDrawLine(
                                    lastBitmapPos,
                                    lastBitmapPos
                                )
                            }
                        }

                        PointerEventType.Move -> {
                            change.consume()
                            val bitmapPos = screenToBitmap(
                                change.position,
                                canvasOffset.value,
                                scaling.value,
                                image
                            )
                            val wasDrawing = isDrawing
                            isDrawing = !bitmapPos.isOvershot

                            if ((isDrawing || wasDrawing) && lastBitmapPos != null) {
                                handleDrawLine(
                                    lastBitmapPos,
                                    bitmapPos.lastValidOffset.toOffset()
                                )
                            }
                            lastBitmapPos = bitmapPos.lastValidOffset.toOffset()
                        }


                        PointerEventType.Release -> {
                            isDrawing = false
                            lastBitmapPos = null
                            change.consume()
                        }
                    }
                }

                2 -> { // Two-finger pan
                    val p1 = pointers[0]
                    val p2 = pointers[1]
                    p1.consume()
                    p2.consume()

                    if ((p1.position - p2.position).getDistance() < 72.dp.toPx()) {
                        break
                    }

                    // Move: midpoint delta
                    val prevMid = (p1.previousPosition + p2.previousPosition) / 2f
                    val currentMid = (p1.position + p2.position) / 2f
                    val delta = currentMid - prevMid
                    canvasOffset.value += delta

                    // Zoom: distance ratio
                    val prevDistance = (p1.previousPosition - p2.previousPosition).getDistance()
                    val currentDistance = (p1.position - p2.position).getDistance()
                    val zoomFactor = currentDistance / prevDistance
                    scaling.value = (scaling.value * zoomFactor).coerceIn(0.1f, 10f)
                }

                else -> Unit
            }
        }
    }
}


//        val cellWidth = size.width / cellsPerRow
//        val cellHeight = size.height / cellsPerColumn
//
//        // Draw grid
//        for (i in 0..cellsPerRow) {
//            drawLine(
//                color = Color(0xFF757575),
//                start = Offset(i * cellWidth, 0f),
//                end = Offset(i * cellWidth, size.height),
//                strokeWidth = 1f
//            )
//        }
//
//        for (i in 0..cellsPerColumn) {
//            drawLine(
//                color = Color(0xFF757575),
//                start = Offset(0f, i * cellHeight),
//                end = Offset(size.width, i * cellHeight),
//                strokeWidth = 1f
//            )
//        }
//
//        // Draw some example pixels (checkerboard pattern)
//        for (row in 0 until cellsPerColumn) {
//            for (col in 0 until cellsPerRow) {
//                val isGray = (row + col) % 2 == 0
//                if (isGray) {
//                    drawRect(
//                        color = Color(0xFF9E9E9E),
//                        topLeft = Offset(col * cellWidth, row * cellHeight),
//                        size = Size(cellWidth, cellHeight)
//                    )
//                } else {
//                    drawRect(
//                        color = Color(0xFFBDBDBD),
//                        topLeft = Offset(col * cellWidth, row * cellHeight),
//                        size = Size(cellWidth, cellHeight)
//                    )
//                }
//            }
//        }