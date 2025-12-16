package ua.ipze.kpi.part.services.drawing.view

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ua.ipze.kpi.part.database.layer.Layer
import ua.ipze.kpi.part.views.DatabaseViewModel

data class DrawingAmountOfSteps(val backward: UInt, val forward: UInt)
data class CurrentActiveLayer(val layer: Layer, val indexInArray: UInt)
data class CachedBitmapImage(val image: ImageBitmap, val isVisible: Boolean)

abstract class IDrawingViewModel() : ViewModel() {
    // construct
    abstract fun initialize(
        historyLength: UInt,
        pixelsPerPixelCell: UInt,
        id: Long,
        databaseViewModel: DatabaseViewModel,
        closePageOnFailure: () -> Unit
    )

    /**
     * Describes if is [initialize] is finished (return false in other)
     */
    abstract fun isReady(): StateFlow<Boolean>

    // data
    abstract fun getWidthAmountPixels(): UInt
    abstract fun getCurrentActiveLayerIndex(): StateFlow<UInt>
    abstract fun getHeightAmountPixels(): UInt
    abstract fun getOperativeData(): OperativeData

    abstract fun addLayer(layer: Layer)
    abstract fun deleteLayer(index: UInt)
    abstract fun getLayers(): StateFlow<List<Layer>>
    abstract fun getCurrentActiveLayer(): StateFlow<CurrentActiveLayer?>
    abstract fun getRealPixelsPerDrawingPixel(): UInt
    abstract fun swapLayers(a: UInt, b: UInt)
    abstract fun setActiveLayer(index: UInt)
    abstract fun setVisibilityOfLayer(index: UInt, isVisible: Boolean)
    abstract fun setLockOnLayer(index: UInt, isLocked: Boolean)
    abstract fun setLayerName(index: UInt, name: String)
    abstract fun getPalette(): StateFlow<List<Color>>
    abstract fun setPalette(colors: List<Color>)
    abstract fun getProjectName(): String

    // line drawing
    /**
     * if start == end - draws one pixel
     */
    abstract fun drawLine(start: Offset, end: Offset, color: Color)

    /**
     * if start == end - draws one pixel
     */
    abstract fun clearLine(start: Offset, end: Offset)

    // pick color
    abstract fun pickColorAt(offset: IntOffset): Color

    // move
    abstract fun startMovePixels(selectArea: Rect)
    abstract fun movePixels(deltaPath: Offset) // second canvas
    abstract fun endMovePixels()

    // history
    abstract fun stepBack()
    abstract fun stepForward()

    // get state
    abstract fun getAmountOfSteps(): StateFlow<DrawingAmountOfSteps>

    // clear image
    abstract fun clearImage()

    abstract fun saveProject()
    abstract fun toPng(): ByteArray

    // internal DON'T USE
    @Suppress("FunctionName")
    abstract fun __INTERNAL_getCachedBitmapImage(): List<CachedBitmapImage>

    @Suppress("PropertyName")
    abstract val __INTERNAL_bitmapVersion: MutableStateFlow<UInt>
}



