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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntOffset
import androidx.core.graphics.createBitmap
import androidx.core.graphics.get
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.ipze.kpi.part.database.layer.Layer
import ua.ipze.kpi.part.database.project.Project
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
            project = data.project
            widthAmountPixels = project.width.toUInt()
            heightAmountPixels = project.height.toUInt()

            val bitmapResults = data.layers.mapIndexedNotNull { index, value ->
                getBitmap(
                    index,
                    value.id,
                    data,
                    pixelsPerPixelCell,
                    index == 0
                )
            }.reversed()
            if (bitmapResults.size != data.layers.size) {
                closePageOnFailure()
                return@launch
            }
            bitmaps = bitmapResults.toMutableList()
            canvases = bitmaps.map { Canvas(it) }.toMutableList()

            realPixelsPerDrawPixel = pixelsPerPixelCell
            databaseView = databaseViewModel
            ready.set(true)
            triggerRedraw()


            layers.value = data.layers
            activeLayerIndex.value = 0u

        }

        // blocking coroutines
        viewModelScope.launch {
            layers.combine(activeLayerIndex) { layersList, index ->
                val i = index.toInt()
                val layer = if (i in layersList.indices) layersList[i] else null

                if (layer == null) null
                else CurrentActiveLayer(layer = layer, indexInArray = index)
            }.collect({ activeLayer.value = it })
        }
    }

    private fun getBitmap(
        index: Int,
        id: Long,
        data: DatabaseProjectWithLayers,
        pixelsPerPixelCell: UInt,
        isInitial: Boolean = false
    ): Bitmap? {
        if (data.layers[index].imageData.isEmpty()) {
            val newBitmap = createBitmap(
                (widthAmountPixels * pixelsPerPixelCell).toInt(),
                (heightAmountPixels * pixelsPerPixelCell).toInt()
            )
            Canvas(newBitmap).drawPaint(Paint().also {
                it.color =
                    if (isInitial) {
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

    private lateinit var project: Project
    private lateinit var bitmaps: MutableList<Bitmap>
    private lateinit var canvases: MutableList<Canvas>
    private lateinit var amountOfSteps: MutableStateFlow<DrawingAmountOfSteps>
    private var historyLength: UInt = 0u
    private var widthAmountPixels: UInt = 0u
    private var heightAmountPixels: UInt = 0u
    private var realPixelsPerDrawPixel: UInt = 0u
    private lateinit var operativeData: OperativeData
    private lateinit var databaseView: DatabaseViewModel

    private val layers = MutableStateFlow<List<Layer>>(listOf())
    private val activeLayerIndex = MutableStateFlow<UInt>(0u)

    private val activeLayer = MutableStateFlow<CurrentActiveLayer?>(null)

    // ----------------------------------------------------
    override fun getLayers(): StateFlow<List<Layer>> = layers.asStateFlow()
    override fun getCurrentActiveLayerIndex(): StateFlow<UInt> = activeLayerIndex.asStateFlow()
    override fun getRealPixelsPerDrawingPixel(): UInt = realPixelsPerDrawPixel

    override fun getCurrentActiveLayer(): MutableStateFlow<CurrentActiveLayer?> = activeLayer

    override fun addLayer(layer: Layer) {
        if (!ready.get()) return

        layers.update {
            bitmaps.add(
                0,
                createBitmap(
                    project.width * realPixelsPerDrawPixel.toInt(),
                    project.height * realPixelsPerDrawPixel.toInt()
                )
            )
            canvases.add(0, Canvas(bitmaps.first()))
            return@update listOf(layer) + it
        }
        triggerRedraw()
    }

    override fun deleteLayer(index: UInt) {
        if (!ready.get()) return
        if (index.toInt() >= bitmaps.size) return

        layers.update {
            bitmaps.removeAt(index.toInt())
            canvases.removeAt(index.toInt())
            it.toMutableList().filterIndexed { indexFilter, _ -> indexFilter != index.toInt() }
                .toList()
        }
        triggerRedraw()
    }

    override fun swapLayers(a: UInt, b: UInt) {
        if (!ready.get()) return
        layers.update {
            val layersAmount = it.size.toUInt()
            if (a >= layersAmount || b >= layersAmount || a == b) {
                return@update it
            }
            val tempBitmap = bitmaps[a.toInt()]
            bitmaps[a.toInt()] = bitmaps[b.toInt()]
            bitmaps[b.toInt()] = tempBitmap

            val tempCanvas = canvases[a.toInt()]
            canvases[a.toInt()] = canvases[b.toInt()]
            canvases[b.toInt()] = tempCanvas

            return@update it.toMutableList().apply {
                val tmp = this[a.toInt()]
                this[a.toInt()] = this[b.toInt()]
                this[b.toInt()] = tmp
            }.toList()
        }
        triggerRedraw()

    }

    override fun setActiveLayer(index: UInt) {
        if (!ready.get()) return
        activeLayerIndex.value = index
    }

    override fun setVisibilityOfLayer(index: UInt, isVisible: Boolean) {
        if (!ready.get()) return

        val i = index.toInt()
        layers.value = layers.value.mapIndexed { idx, layer ->
            if (idx == i) layer.copy(visibility = isVisible)
            else layer
        }
        triggerRedraw()
    }

    override fun setLockOnLayer(index: UInt, isLocked: Boolean) {
        if (!ready.get()) return

        val i = index.toInt()
        layers.value = layers.value.mapIndexed { idx, layer ->
            if (idx == i) layer.copy(lock = isLocked)
            else layer
        }
    }

    override fun setLayerName(index: UInt, name: String) {
        if (!ready.get()) return

        val i = index.toInt()
        layers.value = layers.value.mapIndexed { idx, layer ->
            if (idx == i) layer.copy(name = name)
            else layer
        }
    }
    // ----------------------------------------------------

    private fun safeStep() {
        if (!ready.get()) return


    }

    private fun triggerRedraw() {
        // wrap-overflow is desirable
        __INTERNAL_bitmapVersion.value += 1u
    }

    // Internal

    override fun __INTERNAL_getCachedBitmapImage(): List<CachedBitmapImage> {
        if (!ready.get()) return listOf(CachedBitmapImage(defaultBitmap.asImageBitmap(), true))
        return bitmaps.mapIndexed { index, it ->
            CachedBitmapImage(
                it.asImageBitmap(),
                layers.value.getOrNull(index)?.visibility ?: false
            )
        }
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
        if (activeLayerIndex.value.toInt() >= canvases.size) {
            Log.e(
                Tag,
                "Skipping drawing line, because queried canvas " +
                        "(index: ${activeLayerIndex.value}) is out of bounds of array of canvases"
            )
        }
        if (layers.value[activeLayerIndex.value.toInt()].lock) return


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
        if (activeLayerIndex.value.toInt() >= canvases.size) {
            Log.i(
                Tag,
                "Skipping cleaning line, because queried canvas " +
                        "(index: ${activeLayerIndex.value}) is out of bounds of array of canvases"
            )
        }

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
        if (activeLayerIndex.value.toInt() >= canvases.size) {
            Log.i(
                Tag,
                "Skipping picking color, because queried canvas " +
                        "(index: ${activeLayerIndex.value}) is out of bounds of array of canvases"
            )
        }

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