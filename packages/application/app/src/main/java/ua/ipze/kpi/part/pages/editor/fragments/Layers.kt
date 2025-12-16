package ua.ipze.kpi.part.pages.editor.fragments

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.database.layer.Layer
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import ua.ipze.kpi.part.services.drawing.view.DrawingViewModel
import ua.ipze.kpi.part.ui.theme.pixelBorder

@Composable
fun LayersPanel(
    width: Int, height: Int,
    drawingViewModel: DrawingViewModel
) {
    val data = BasePageDataProvider.current

    val scrollStateH = rememberScrollState()
    val lazyListState = rememberLazyListState()

    val layers by drawingViewModel.getLayers().collectAsState()
    val activeIndex by drawingViewModel.getCurrentActiveLayerIndex().collectAsState()

    Surface(
        color = Color(0xFF424242),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                // Action buttons
                Log.d("red", "redrown2")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = {
                        drawingViewModel.addLayer(
                            Layer(
                                name = "New Layer",
                                visibility = true,
                                lock = false,
                                imageData = ByteArray(width * height)
                            )
                        )
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.add_button_icon),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { drawingViewModel.deleteLayer(activeIndex) }) {
                        Icon(
                            painter = painterResource(R.drawable.trash_bin),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .height(300.dp)
                ) {
                    itemsIndexed(
                        items = layers
                    ) { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .background(
                                    if (index.toUInt() == activeIndex) Color(
                                        0xFF505050
                                    ) else Color(0xFF303030),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(4.dp)
                                .clickable(onClick = {
                                    drawingViewModel.setActiveLayer(index.toUInt())
                                }),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                drawingViewModel.setVisibilityOfLayer(
                                    index.toUInt(),
                                    !item.visibility
                                )
                            }) {
                                Icon(
                                    painter = painterResource(if (item.visibility) R.drawable.open_eye else R.drawable.hidden_eye),
                                    contentDescription = null,
                                    tint = Color(0xFF9E9E9E),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            BasicTextField(
                                value = layers[index].name,
                                onValueChange = { it ->
                                    drawingViewModel.setLayerName(
                                        index.toUInt(),
                                        it
                                    )
                                },
                                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                                modifier = Modifier
                                    .weight(1f)
                                    .pixelBorder(borderWidth = 1.5.dp),
                                enabled = index.toUInt() == activeIndex
                            )

                            IconButton(onClick = {
                                drawingViewModel.setLockOnLayer(index.toUInt(), !item.lock)
                            }) {
                                Icon(
                                    painter = painterResource(if (item.lock) R.drawable.lock_closed else R.drawable.lock_open),
                                    contentDescription = null,
                                    tint = Color(0xFF9E9E9E),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            IconButton(onClick = {
                                if (index - 1 >= 0) {
                                    drawingViewModel.swapLayers(
                                        index.toUInt(),
                                        (index - 1).toUInt()
                                    )
                                }
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.dropdown_up_arrow),
                                    contentDescription = null,
                                    tint = Color(0xFF9E9E9E),
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }

                            IconButton(onClick = {
                                if (index + 1 < layers.size) {
                                    drawingViewModel.swapLayers(
                                        index.toUInt(),
                                        (index + 1).toUInt()
                                    )
                                }
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.dropdown_down_arrow),
                                    contentDescription = null,
                                    tint = Color(0xFF9E9E9E),
                                    modifier = Modifier
                                        .size(24.dp)
                                )
                            }
                        }
                    }

                }

//            HorizontalDivider(
//                modifier = Modifier.padding(vertical = 8.dp),
//                thickness = 1.dp,
//                color = Color(0xFF757575)
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.clickable { TODO("Додати мердж вгору") }
//                ) {
//                    Icon(
//                        painter = painterResource(R.drawable.merge_arrow_up),
//                        contentDescription = null,
//                        tint = Color(0xffffffff),
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        localizedStringResource(R.string.merge_up, data.language),
//                        color = Color(0xffffffff),
//                        fontSize = 14.sp,
//                        fontFamily = FontFamily.Monospace
//                    )
//                }
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.clickable { TODO("Додати мердж вниз") }
//                ) {
//                    Icon(
//                        painter = painterResource(R.drawable.merge_arrow_down),
//                        contentDescription = null,
//                        tint = Color(0xffffffff),
//                        modifier = Modifier.size(20.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        localizedStringResource(R.string.merge_down, data.language),
//                        color = Color(0xffffffff),
//                        fontSize = 14.sp,
//                    )
//                }
//            }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}