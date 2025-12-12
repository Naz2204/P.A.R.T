package ua.ipze.kpi.part.pages.drawing.fragments

import android.graphics.ColorSpace
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.Rgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.ui.theme.pixelBorder

@Composable
fun BottomBar(
    onLayersClick: () -> Unit,
    onEyeClick: () -> Unit,
    layerHidden: Boolean,
    layersOpen: Boolean,
    imageSize: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF60646A))
                    .padding(top=0.dp, bottom = 0.dp, start = 5.dp, end = 5.dp)
            ) {
                Text(
                    text = "Layer 1",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .pixelBorder(borderWidth = 2.dp)
                        .padding(5.dp)
                        .weight(0.7f)
                )
                IconButton(onClick = onEyeClick) {
                    Icon(
                        painter = painterResource(
                            if (layerHidden) R.drawable.hidden_eye
                            else R.drawable.open_eye
                        ),
                        contentDescription = "Hide/Unhide",
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
                        contentDescription = "Layers",
                        tint = Color(0xffffffff),
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().background(Color(0xff383d43))
                    .padding(top=3.dp, bottom = 4.dp, start = 6.dp, end = 6.dp)
                ) {
                    Text(
                        text = imageSize,
                        color = Color(0xffffffff),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )

                    Text(
                        text = "13:11",
                        color = Color(0xffffffff),
                        fontSize = 14.sp,
                    )
                }
        }
    }
}
