package ua.ipze.kpi.part.services.drawing

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import androidx.core.graphics.createBitmap
import ua.ipze.kpi.part.services.drawing.view.CachedBitmapImage
import ua.ipze.kpi.part.services.drawing.view.IDrawingViewModel

@Composable
private fun IDrawingViewModel.rememberImage(): List<CachedBitmapImage> {
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
    val yClipped = yReal.coerceIn(0, image.height - 1)

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

    val images = view.rememberImage()
    val scaling = remember { mutableFloatStateOf(1f) }
    val offset = remember { mutableStateOf(Offset.Zero) }
    var wasCentered by remember { mutableStateOf(false) }
    var canvasSize by remember { mutableStateOf(Size(1f, 1f)) }

    val firstOrNull = images.getOrNull(0)
    LaunchedEffect(firstOrNull?.image?.width, firstOrNull?.image?.height) {
        wasCentered = false
    }

    val realPixelsPerDrawingPixel = view.getRealPixelsPerDrawingPixel()
    val chessBoardBitmap = remember(realPixelsPerDrawingPixel, firstOrNull?.image) {
        val backgroundSize =
            IntSize(firstOrNull?.image?.width ?: 1, firstOrNull?.image?.height ?: 1)
        drawBackground(realPixelsPerDrawingPixel, backgroundSize)
    }


    if (images.isEmpty()) return
    val anyImage = images[0].image

    Canvas(
        modifier = modifier
            .clipToBounds()
            .pointerInput(Unit) {
                drawAndPanPointerInput(offset, scaling, anyImage, canvasSize, handleDrawLine)
            }
            .onSizeChanged {
                canvasSize = it.toSize()
                if (wasCentered) return@onSizeChanged
                wasCentered = true // correct code, ignore linter

                val scaleX = it.width.toFloat() / anyImage.width
                val scaleY = it.height.toFloat() / anyImage.height
                Log.d("aaa", "$scaleX $scaleY ${it.height} ${it.height}")
                Log.d("aaa", "${anyImage.height} ${anyImage.width}")
                scaling.floatValue = minOf(scaleX, scaleY)

                val scaledWidth = anyImage.width * scaling.floatValue
                val scaledHeight = anyImage.height * scaling.floatValue
                offset.value = Offset(
                    x = (it.width - scaledWidth) / 2f,
                    y = (it.height - scaledHeight) / 2f
                )
            }
    ) {
        val drawBitmapImage = { image: ImageBitmap ->
            drawImage(
                image = image,
                srcOffset = IntOffset.Zero,
                srcSize = IntSize(
                    image.width,
                    image.height
                ),
                dstOffset = IntOffset(
                    offset.value.x.toInt(),
                    offset.value.y.toInt()
                ),
                dstSize = IntSize(
                    (anyImage.width * scaling.floatValue).toInt(),
                    (anyImage.height * scaling.floatValue).toInt()
                ),
                filterQuality = FilterQuality.None
            )
        }

        drawBitmapImage(chessBoardBitmap.asImageBitmap())
        images.asReversed().forEach {
            if (it.isVisible) drawBitmapImage(it.image)
        }
    }
}

private fun drawBackground(
    realPixelsPerDrawingPixel: UInt = 0u,
    backgroundPixelAmount: IntSize,
): Bitmap {
    val result = createBitmap(backgroundPixelAmount.width, backgroundPixelAmount.height)
    val drawer = android.graphics.Canvas(result)

    val realPixelAmount = realPixelsPerDrawingPixel.coerceAtLeast(1u).toInt()
    val cellsPerRow = backgroundPixelAmount.width / realPixelAmount
    val cellsPerColumn = backgroundPixelAmount.width / realPixelAmount

    // Chessboard pattern
    val grayPaint = android.graphics.Paint().also {
        it.color = Color(0xFF9E9E9E).toArgb()
    }
    val whitePaint = android.graphics.Paint().also {
        it.color = Color(0xFFBDBDBD).toArgb()
    }

    for (row in 0 until cellsPerColumn) {
        for (col in 0 until cellsPerRow) {
            val isGray = (row + col) % 2 == 0
            val rectangle = android.graphics.Rect(
                row * realPixelAmount,
                col * realPixelAmount,
                (row + 1) * realPixelAmount,
                (col + 1) * realPixelAmount
            )
            drawer.drawRect(
                rectangle,
                if (isGray) grayPaint else whitePaint
            )
        }
    }

    return result
}

// Draw grid
//    for (i in 0..cellsPerRow) {
//        drawLine(
//            color = Color(0xFF757575),
//            start = offset + Offset(i * pixelSize, 0f),
//            end = offset + Offset(i * pixelSize, size.height),
//            strokeWidth = 3f
//        )
//    }
//
//    for (i in 0..cellsPerColumn) {
//        drawLine(
//            color = Color(0xFF757575),
//            start = offset + Offset(0f, i * pixelSize),
//            end = offset + Offset(size.width, i * pixelSize),
//            strokeWidth = 3f
//        )
//    }


suspend fun PointerInputScope.drawAndPanPointerInput(
    canvasOffset: MutableState<Offset>,
    scaling: MutableState<Float>,
    image: ImageBitmap,
    canvasSize: Size,
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
                    isDrawing = false
                    lastBitmapPos = null

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

                    val oldScale = scaling.value
                    scaling.value = (oldScale * zoomFactor).coerceIn(0.1f, 40f)
                    val effectiveZoom = scaling.value / oldScale
                    canvasOffset.value += (currentMid - canvasOffset.value) * (1 - effectiveZoom)

                    // Normalizing
                    val realWidth = image.width * scaling.value
                    val realHeight = image.height * scaling.value
                    val negativeMultiple = 0.8f
                    val positiveMultiple = 1 - negativeMultiple


                    canvasOffset.value =
                        Offset(
                            canvasOffset.value.x.coerceIn(
                                -realWidth * negativeMultiple,
                                canvasSize.width - realWidth * positiveMultiple
                            ),
                            canvasOffset.value.y.coerceIn(
                                -realHeight * negativeMultiple,
                                canvasSize.height - realHeight * positiveMultiple
                            )
                        )
                }

                else -> {
                    isDrawing = false
                    lastBitmapPos = null
                }
            }
        }
    }
}


