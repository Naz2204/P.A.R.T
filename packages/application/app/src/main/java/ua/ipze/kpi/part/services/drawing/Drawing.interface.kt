package ua.ipze.kpi.part.services.drawing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.StateFlow
import java.io.File

data class DrawingAmountOfSteps(val backward: UInt, val forward: UInt) {}

abstract class IDrawing(val historyLength: UInt, val widthAmountPixels: UInt, val heightAmountPixels: UInt, val handleGesture: () -> Unit) {
    // line drawing
    /**
     * if start == end - draws one pixel
     */
    abstract fun drawLine(start: Offset, end: Offset, color: Color): Unit;
    /**
     * if start == end - draws one pixel
     */
    abstract fun clearLine(start: Offset, end: Offset): Unit;

    // pick color
    abstract fun pickColorAt(offset: Offset): Color;

    // move
    abstract fun startMovePixels(selectArea: Rect);
    abstract fun movePixels(deltaPath: Offset); // second canvas
    abstract fun endMovePixels();

    // history
    abstract fun stepBack(): Unit;
    abstract fun stepForward(): Unit;

    // get state
    abstract fun getAmountOfSteps(): StateFlow<DrawingAmountOfSteps>;

    // clear
    abstract fun clear(): Unit;

    // draw
    @Composable
    abstract fun DrawCanvas(modifier: Modifier, offset: Offset, scaling: Float): Unit;

    // work with files
    abstract fun load(file: File): Result<Unit>;
    abstract fun storeToPng(): ByteArray;
}


