package ua.ipze.kpi.part.services.drawing

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset

abstract class IDrawing(val historyLength: UInt, val width: Unit, val height: Unit) {
    abstract fun drawLine(x: Float, y: Float): Unit;
    abstract fun startMovePixels();
    abstract fun movePixels(path: Offset);
    abstract fun endMovePixels();

    abstract fun stepBack();
    abstract fun stepForward();

    abstract fun clear(): Unit;

    @Composable
    abstract fun drawCanvas(offset: Offset, scaling: Float)
}


