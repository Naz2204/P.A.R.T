package ua.ipze.kpi.part.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.pixelBorder(
    borderWidth: Dp = 8.dp,
    outerColor: Color = Color.Gray,
    innerColor: Color = Color.LightGray,
    backgroundColor: Color = Color.DarkGray,
): Modifier = this.then(
    Modifier.drawBehind {
        val w = size.width
        val h = size.height
        val s = borderWidth.toPx()

        drawRect(
            color = outerColor,
            topLeft = Offset(0f, s),
            size = Size(w, h - 2 * s),
        )

        drawRect(
            color = outerColor,
            topLeft = Offset(s, 0f),
            size = Size(w - 2 * s, h),
        )

        drawRect(
            color = innerColor,
            topLeft = Offset(s, s),
            size = Size(w - 2 * s, h - 2 * s),
        )
    }
).padding(borderWidth * 2).background(backgroundColor)