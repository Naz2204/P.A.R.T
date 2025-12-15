package ua.ipze.kpi.part.services.drawing.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
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
import androidx.core.graphics.get
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.ipze.kpi.part.database.layer.Layer
import ua.ipze.kpi.part.views.DatabaseProjectWithLayers
import ua.ipze.kpi.part.views.DatabaseViewModel
import java.util.concurrent.atomic.AtomicBoolean

private val Tag = DrawingViewModel::class.simpleName ?: ""

val defaultBitmap = createBitmap(100, 100)

class DrawingViewModel() : IDrawingViewModel() {

    private val initialized = AtomicBoolean(false)
    private val ready = AtomicBoolean(false)

    override fun initialize(
        historyLength: UInt,
        pixelsPerPixelCell: UInt,
        id: Long,
        databaseViewModel: DatabaseViewModel,
        closePageOnFailure: () -> Unit
    ) {
        if (!initialized.compareAndSet(false, true)) {
            Log.e(Tag, "Got second init")
            return
        }

        this.operativeData = OperativeData(viewModelScope)
        this.historyLength = historyLength
        amountOfSteps = MutableStateFlow(DrawingAmountOfSteps(0u, 0u))

        viewModelScope.launch {
            val data = databaseViewModel.getProjectWithLayers(id)
            if (data == null) {
                closePageOnFailure()
                Log.e(Tag, "Failed to get value from db for id: $id")
                return@launch
            }

            widthAmountPixels = data.project.width.toUInt()
            heightAmountPixels = data.project.height.toUInt()

            val bitmapResults = data.layers.mapIndexedNotNull { index, value ->
                getBitmap(
                    index,
                    value.id,
                    data,
                    pixelsPerPixelCell
                )
            }
            if (bitmapResults.size != data.layers.size) {
                closePageOnFailure()
                return@launch
            }
            bitmaps = bitmapResults
            canvases = bitmaps.map { Canvas(it) }

            realPixelsPerDrawPixel = pixelsPerPixelCell
            databaseView = databaseViewModel
            ready.set(true)
            triggerRedraw()


            layers = MutableStateFlow(data.layers)
            activeLayerIndex = MutableStateFlow(0u)

            activeLayer = layers.combine(activeLayerIndex) { layersList, index ->
                val i = index.toInt()
                val layer = if (i in layersList.indices) layersList[i] else null

                if (layer == null) null
                else CurrentActiveLayer(layer = layer, indexInArray = index)
            }.stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                null
            )
        }
    }

    private fun getBitmap(
        index: Int,
        id: Long,
        data: DatabaseProjectWithLayers,
        pixelsPerPixelCell: UInt,
    ): Bitmap? {
        if (data.layers[index].imageData.isEmpty()) {
            val newBitmap = createBitmap(
                (widthAmountPixels * pixelsPerPixelCell).toInt(),
                (heightAmountPixels * pixelsPerPixelCell).toInt()
            )
            Canvas(newBitmap).drawPaint(Paint().also {
                it.color =
                    if (index == 0) {
                        Color(data.project.baseColor.toULong()).toArgb()
                    } else {
                        Color.Transparent.toArgb()
                    }
            })
            return newBitmap
        }

        val decodedBitmap = BitmapFactory.decodeByteArray(
            data.layers[index].imageData, 0,
            data.layers[index].imageData.size
        )?.copy(
            Bitmap.Config.ARGB_8888, true
        )

        if (decodedBitmap == null) {
            Log.e(Tag, "Failed to decode 1st layer from array id: $id")
            return null
        }

        return decodedBitmap
    }

    // ----------------------------------------------------
    // data

    private lateinit var bitmaps: List<Bitmap>
    private lateinit var canvases: List<Canvas>
    private lateinit var amountOfSteps: MutableStateFlow<DrawingAmountOfSteps>
    private var historyLength: UInt = 0u
    private var widthAmountPixels: UInt = 0u
    private var heightAmountPixels: UInt = 0u
    private var realPixelsPerDrawPixel: UInt = 0u
    private lateinit var operativeData: OperativeData
    private lateinit var databaseView: DatabaseViewModel

    private lateinit var layers: MutableStateFlow<List<Layer>>
    private lateinit var activeLayerIndex: MutableStateFlow<UInt>
    private lateinit var activeLayer: StateFlow<CurrentActiveLayer?>

    // ----------------------------------------------------
    override fun getLayers(): StateFlow<List<Layer>> = layers.asStateFlow()
    override fun getCurrentActiveLayer(): StateFlow<CurrentActiveLayer?> = activeLayer
    override fun getCurrentActiveLayerIndex(): StateFlow<UInt> = activeLayerIndex.asStateFlow()

    override fun addLayer(layer: Layer) {
        layers.update { return@update listOf(layer) + it }
        triggerRedraw()
    }

    override fun deleteLayer(index: UInt) {
        layers.update {
            it.toMutableList().filterIndexed { indexFilter, _ -> indexFilter == index.toInt() }
                .toList()
        }
        triggerRedraw()
    }

    override fun swapLayers(a: UInt, b: UInt) {
        layers.update {
            val layersAmount = it.size.toUInt()
            if (a >= layersAmount || b >= layersAmount || a == b) {
                return@update it
            }

            return@update it.toMutableList().apply {
                val tmp = this[a.toInt()]
                this[a.toInt()] = this[b.toInt()]
                this[b.toInt()] = tmp
            }.toList()
        }
        triggerRedraw()

    }

    override fun setActiveLayer(index: UInt) {
        activeLayerIndex.value = index
    }

    override fun setVisibilityOfLayer(index: UInt, isVisible: Boolean) {
        val i = index.toInt()
        layers.value = layers.value.mapIndexed { idx, layer ->
            if (idx == i) layer.copy(visibility = isVisible)
            else layer
        }
        triggerRedraw()
    }

    override fun setLockOnLayer(index: UInt, isLocked: Boolean) {
        val i = index.toInt()
        layers.value = layers.value.mapIndexed { idx, layer ->
            if (idx == i) layer.copy(lock = isLocked)
            else layer
        }
    }


    // ----------------------------------------------------

    private fun safeStep() {
    }

    private fun triggerRedraw() {
        // wrap-overflow is desirable
        __INTERNAL_bitmapVersion.value += 1u
    }

    // Internal

    override fun __INTERNAL_getCachedBitmapImage(): List<ImageBitmap> {
        if (!ready.get()) return listOf(defaultBitmap.asImageBitmap())
        return bitmaps.map { it.asImageBitmap() }
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
        if (!ready.get()) return

        val startScaled = Offset(
            start.x / this.realPixelsPerDrawPixel.toInt(),
            start.y / this.realPixelsPerDrawPixel.toInt()
        )
        val endScaled = Offset(
            end.x / this.realPixelsPerDrawPixel.toInt(),
            end.y / this.realPixelsPerDrawPixel.toInt()
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
                Offset(
                    it.x * realPixelsPerDrawPixel.toFloat(),
                    it.y * realPixelsPerDrawPixel.toFloat()
                )
            canvases[activeLayerIndex.value.toInt()].drawRect(
                RectF(
                    topLeft.x,
                    topLeft.y,
                    topLeft.x + realPixelsPerDrawPixel.toFloat(),
                    topLeft.y + realPixelsPerDrawPixel.toFloat(),
                ), paint
            )
        }
        triggerRedraw()
    }

    override fun clearLine(start: Offset, end: Offset) {
        if (!ready.get()) return

        val startScaled = Offset(
            start.x / realPixelsPerDrawPixel.toInt(),
            start.y / realPixelsPerDrawPixel.toInt()
        )
        val endScaled = Offset(
            end.x / realPixelsPerDrawPixel.toInt(),
            end.y / realPixelsPerDrawPixel.toInt()
        )

        val scaledPixels = bresenhamLine(
            startScaled.x.toInt(),
            startScaled.y.toInt(),
            endScaled.x.toInt(),
            endScaled.y.toInt()
        )

        val paint = Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            isAntiAlias = false
        }

        scaledPixels.forEach {
            val topLeft = Offset(
                it.x * realPixelsPerDrawPixel.toFloat(),
                it.y * realPixelsPerDrawPixel.toFloat()
            )

            canvases[activeLayerIndex.value.toInt()].drawRect(
                RectF(
                    topLeft.x,
                    topLeft.y,
                    topLeft.x + realPixelsPerDrawPixel.toInt(),
                    topLeft.y + realPixelsPerDrawPixel.toInt()
                ),
                paint
            )
        }

        paint.xfermode = null
        triggerRedraw()
    }

    // ----------------------------------------------------


    override fun pickColorAt(offset: IntOffset): Color {
        if (!ready.get()) return Color.Transparent

        if (widthAmountPixels.toInt() <= offset.x || offset.x < 0) return Color.Transparent
        if (heightAmountPixels.toInt() <= offset.y || offset.y < 0) return Color.Transparent
        return Color(bitmaps[activeLayerIndex.value.toInt()][offset.x * realPixelsPerDrawPixel.toInt(), offset.y * realPixelsPerDrawPixel.toInt()])
    }

    // ----------------------------------------------------


    override fun startMovePixels(selectArea: Rect) {
        if (!ready.get()) return

    }

    override fun movePixels(deltaPath: Offset) {
        if (!ready.get()) return

    }

    override fun endMovePixels() {
        if (!ready.get()) return

    }

    // ----------------------------------------------------


    override fun stepBack() {
        if (!ready.get()) return

    }

    override fun stepForward() {
        if (!ready.get()) return

    }

    override fun getAmountOfSteps(): StateFlow<DrawingAmountOfSteps> {
        if (!ready.get()) return TODO("a")
        return TODO("Provide the return value")
    }

    // ----------------------------------------------------

    override fun clearImage() {
        if (!ready.get()) return

    }

    // ----------------------------------------------------

    override fun load(index: UInt, png: ByteArray): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun storeToPng(index: UInt): ByteArray {
        TODO("Not yet implemented")
    }


    override fun getWidthAmountPixels(): UInt {
        if (!ready.get()) return 0u

        return widthAmountPixels
    }

    override fun getHeightAmountPixels(): UInt {
        if (!ready.get()) return 0u

        return heightAmountPixels
    }

    override fun getOperativeData(): OperativeData {
        return operativeData
    }
}