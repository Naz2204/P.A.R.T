package ua.ipze.kpi.part.pages.editor.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.services.drawing.view.IDrawingViewModel
import ua.ipze.kpi.part.ui.theme.pixelBorder

@Composable
fun BottomBar(
    drawingViewModel: IDrawingViewModel,
    onLayersClick: () -> Unit,
    layersOpen: Boolean
) {
    drawingViewModel.getOperativeData().start()
    val activeLayer = drawingViewModel.getCurrentActiveLayer().collectAsState()
    val maxBottomBar = 69.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = maxBottomBar),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF60646A))
                .padding(horizontal = 10.dp, vertical = 0.dp)
        ) {
            Text(
                text = "Layer ${activeLayer.value?.layer?.id}",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier
                    .pixelBorder(borderWidth = 2.dp)
                    .padding(5.dp)
                    .weight(0.7f)
            )
            IconButton(onClick = {
                drawingViewModel.setVisibilityOfLayer(
                    drawingViewModel.getCurrentActiveLayerIndex().value,
                    !(activeLayer.value?.layer?.visibility ?: true)
                )
            }) {
                Icon(
                    painter = painterResource(
                        if (activeLayer.value?.layer?.visibility ?: true) R.drawable.open_eye
                        else R.drawable.hidden_eye
                    ),
                    contentDescription = null,
                    tint = Color(0xffffffff),
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = onLayersClick) {
                Icon(
                    painter = painterResource(
                        if (layersOpen) R.drawable.menu_arrow_up
                        else R.drawable.menu_arrow_down
                    ),
                    contentDescription = null,
                    tint = Color(0xffffffff),
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xff383d43))
                .padding(top = 3.dp, bottom = 4.dp, start = 15.dp, end = 20.dp)
        ) {
            Text(
                text = "${drawingViewModel.getWidthAmountPixels()}x${drawingViewModel.getHeightAmountPixels()}",
                color = Color(0xffffffff),
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            val time = drawingViewModel.getOperativeData().time.collectAsState().value
            Text(
                text = "${time / 60}:${time % 60}",
                color = Color(0xffffffff),
                fontSize = 14.sp,
            )
        }

    }
}
