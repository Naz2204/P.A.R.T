package ua.ipze.kpi.part.services.drawing.view

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ua.ipze.kpi.part.views.DatabaseViewModel
import java.io.File

data class DrawingAmountOfSteps(val backward: UInt, val forward: UInt)

abstract class IDrawingViewModel() : ViewModel() {
    // construct
    abstract fun initialize(
        historyLength: UInt,
        widthAmountPixels: UInt,
        heightAmountPixels: UInt,
        pixelsPerPixelCell: UInt,
        databaseViewModel: DatabaseViewModel
    )

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
    abstract fun pickColorAt(offset: Offset): Color

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

    // work with files
    abstract fun load(file: File): Result<Unit>
    abstract fun storeToPng(): ByteArray

    // internal DON'T USE
    @Suppress("FunctionName")
    abstract fun __INTERNAL_getCachedBitmapImage(): ImageBitmap
    abstract val __INTERNAL_bitmapVersion: MutableStateFlow<UInt>
}



