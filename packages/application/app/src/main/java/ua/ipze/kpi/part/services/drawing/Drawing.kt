package ua.ipze.kpi.part.services.drawing

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class Drawing(historyLength: UInt, widthAmountPixels: UInt, heightAmountPixels: UInt, handleGesture: () -> Unit) :
    IDrawing(historyLength, widthAmountPixels, heightAmountPixels, handleGesture) {

    private val bitmap: Bitmap =
        Bitmap.createBitmap(widthAmountPixels.toInt(), heightAmountPixels.toInt(), Bitmap.Config.ARGB_8888);
//    private val amountOfSteps: MutableStateFlow<DrawingAmountOfSteps> = DrawingAmountOfSteps(0, 0);

    init {

    }

    // ----------------------------------------------------

    private fun safeStep(): Unit {

    }

    // ----------------------------------------------------


    override fun drawLine(start: Offset, end: Offset, color: Color): Unit {

    }

    override fun clearLine(start: Offset, end: Offset): Unit {

    }

    // ----------------------------------------------------


    override fun pickColorAt(offset: Offset): Color {

        return TODO("Provide the return value")
    }

    // ----------------------------------------------------


    override fun startMovePixels(selectArea: Rect): Unit {

    }

    override fun movePixels(deltaPath: Offset): Unit {

    }

    override fun endMovePixels(): Unit {

    }

    // ----------------------------------------------------


    override fun stepBack(): Unit {

    }
    override fun stepForward(): Unit {

    }

    override fun getAmountOfSteps(): StateFlow<DrawingAmountOfSteps> {

        return TODO("Provide the return value")
    }

    // ----------------------------------------------------


    override fun clear(): Unit {

    }

    // ----------------------------------------------------


    @Composable
    override fun DrawCanvas(modifier: Modifier, offset: Offset, scaling: Float): Unit {

    }

    // ----------------------------------------------------

    override fun load(file: File): Result<Unit> {

        return TODO("Provide the return value")
    }

    override fun storeToPng(): ByteArray {

        return TODO("Provide the return value")
    }

}