package ua.ipze.kpi.part.services.drawing.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntOffset
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

private val Tag = DrawingViewModel::class.simpleName ?: ""

class DrawingViewModel() :
    IDrawingViewModel() {

    private val initialized = AtomicBoolean(false)

    override fun initialize(
        historyLength: UInt,
        widthAmountPixels: UInt,
        heightAmountPixels: UInt,
        pixelsPerPixelCell: UInt,
    ) {
        if (!initialized.compareAndSet(false, true)) {
            Log.e(Tag, "Got second init")
            return
        }
        bitmap = createBitmap(widthAmountPixels.toInt(), heightAmountPixels.toInt())
        canvas = Canvas(bitmap)
        canvas.drawPaint(Paint().also {
            it.color = Color(200, 124, 122).toArgb()
        })

        amountOfSteps = MutableStateFlow(DrawingAmountOfSteps(0u, 0u))
        this.historyLength = historyLength
        this.widthAmountPixels = widthAmountPixels
        this.heightAmountPixels = heightAmountPixels
        this.pixelsPerPixelCell = pixelsPerPixelCell
        this.databaseViewModel = databaseViewModel
        this.operativeData = OperativeData(viewModelScope)
    }

    // ----------------------------------------------------
    // data

    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private lateinit var amountOfSteps: MutableStateFlow<DrawingAmountOfSteps>
    private var historyLength: UInt = 0u
    private var widthAmountPixels: UInt = 0u
    private var heightAmountPixels: UInt = 0u
    private var pixelsPerPixelCell: UInt = 0u
    private lateinit var operativeData: OperativeData
    private lateinit var databaseViewModel: DatabaseViewModel

    // ----------------------------------------------------

    private fun safeStep() {
    }

    private fun triggerRedraw() {
        // wrap-overflow is desirable
        __INTERNAL_bitmapVersion.value = __INTERNAL_bitmapVersion.value + 1u
    }

    // Internal

    override fun __INTERNAL_getCachedBitmapImage(): ImageBitmap {
        return bitmap.asImageBitmap()
    }

    override val __INTERNAL_bitmapVersion = MutableStateFlow(0u)

    // ----------------------------------------------------f
    // Draw

    private fun bresenhamLine(
        x0: Int, y0: Int,
        x1: Int, y1: Int,
    ): List<IntOffset> {
        val result = mutableListOf<IntOffset>()
        var x = x0
        var y = y0

        val dx = kotlin.math.abs(x1 - x0)
        val dy = kotlin.math.abs(y1 - y0)

        val sx = if (x0 < x1) 1 else -1
        val sy = if (y0 < y1) 1 else -1

        var err = dx - dy

        while (true) {
            result.add(IntOffset(x, y))
            if (x == x1 && y == y1) break

            val e2 = 2 * err
            if (e2 > -dy) {
                err -= dy
                x += sx
            }
            if (e2 < dx) {
                err += dx
                y += sy
            }
        }

        return result.toList()
    }


    override fun drawLine(start: Offset, end: Offset, color: Color) {
        val startScaled = Offset(
            start.x / this.pixelsPerPixelCell.toInt(),
            start.y / this.pixelsPerPixelCell.toInt()
        )
        val endScaled = Offset(
            end.x / this.pixelsPerPixelCell.toInt(),
            end.y / this.pixelsPerPixelCell.toInt()
        )

        val scaledPixels = bresenhamLine(
            startScaled.x.toInt(),
            startScaled.y.toInt(),
            endScaled.x.toInt(),
            endScaled.y.toInt()
        )

        val paint = Paint().also {
            it.color = color.toArgb()
            it.isAntiAlias = false
            it.isFilterBitmap = false
            it.isDither = false
        }
        scaledPixels.forEach {
            val topLeft =
                Offset(it.x * pixelsPerPixelCell.toFloat(), it.y * pixelsPerPixelCell.toFloat())
            canvas.drawRect(
                RectF(
                    topLeft.x,
                    topLeft.y,
                    topLeft.x + pixelsPerPixelCell.toFloat(),
                    topLeft.y + pixelsPerPixelCell.toFloat(),
                ), paint
            )
        }
        triggerRedraw()
    }

    override fun clearLine(start: Offset, end: Offset) {

    }

    // ----------------------------------------------------


    override fun pickColorAt(offset: Offset): Color {

        return TODO("Provide the return value")
    }

    // ----------------------------------------------------


    override fun startMovePixels(selectArea: Rect) {

    }

    override fun movePixels(deltaPath: Offset) {

    }

    override fun endMovePixels() {

    }

    // ----------------------------------------------------


    override fun stepBack() {

    }

    override fun stepForward() {}

    override fun getAmountOfSteps(): StateFlow<DrawingAmountOfSteps> {
        return TODO("Provide the return value")
    }

    // ----------------------------------------------------


    override fun clearImage() {

    }

    // ----------------------------------------------------

    override fun load(file: File): Result<Unit> {

        return TODO("Provide the return value")
    }

    override fun storeToPng(): ByteArray {

        return TODO("Provide the return value")
    }

    override fun getWidthAmountPixels(): UInt {
        return widthAmountPixels
    }

    override fun getHeightAmountPixels(): UInt {
        return heightAmountPixels
    }

    override fun getOperativeData(): OperativeData {
        return operativeData
    }
}