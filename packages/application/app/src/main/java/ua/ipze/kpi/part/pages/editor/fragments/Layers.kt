package ua.ipze.kpi.part.pages.editor.fragments

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.database.layer.Layer
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import kotlin.math.roundToInt

@Composable
fun LayersPanel(
    width: Int, height: Int,
    layerItems: List<Layer>,
    onLayersReordered: (List<Layer>) -> Unit,
    onLayerAdd: (Layer) -> Unit,
    onLayerDelete: (Int) -> Unit
) {
    val data = BasePageDataProvider.current

    var layersList by remember(layerItems) { mutableStateOf(layerItems) }
    var draggedIndex by remember { mutableIntStateOf(-1) }
    var dragOffset by remember { mutableFloatStateOf(0f) }
    var initialDragIndex by remember { mutableIntStateOf(-1) }
    var currentLayer by remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState()

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
                        onLayerAdd(
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
                    IconButton(onClick = { onLayerDelete(currentLayer) }) {
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
                ) {
                    itemsIndexed(
                        items = layersList,
                        key = { _, item -> item.id }
                    ) { index, layerItem ->
                        val isDragging =
                            layersList.indexOfFirst { it.id == layerItem.id } == draggedIndex
                        val currentIndex = layersList.indexOfFirst { it.id == layerItem.id }
                        val offsetY = if (isDragging) dragOffset else 0f

                        Box(
                            modifier = Modifier
                                .offset { IntOffset(0, offsetY.roundToInt()) }
                                .graphicsLayer {
                                    alpha = if (isDragging) 0.7f else 1f
                                    scaleX = if (isDragging) 1.05f else 1f
                                    scaleY = if (isDragging) 1.05f else 1f
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp)
                                    .background(
                                        if (isDragging) Color(0xFF505050) else Color(0xFF303030),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color(0xFF9E9E9E),
                                    modifier = Modifier.size(24.dp)
                                )

                                Text(
                                    text = "${layerItem.name} (ID: ${layerItem.id})",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    modifier = Modifier.weight(1f)
                                )

                                IconButton(onClick = {}) {
                                    Icon(
                                        painter = painterResource(R.drawable.lock_closed),
                                        contentDescription = null,
                                        tint = Color(0xFF9E9E9E),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {},
                                    modifier = Modifier.pointerInput(layerItem.id) {
                                        detectDragGesturesAfterLongPress(
                                            onDragStart = {
                                                val startIndex =
                                                    layersList.indexOfFirst { it.id == layerItem.id }
                                                draggedIndex = startIndex
                                                initialDragIndex = startIndex
                                                dragOffset = 0f
                                            },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                dragOffset += dragAmount.y

                                                val currentDraggedIndex =
                                                    layersList.indexOfFirst { it.id == layerItem.id }

                                                // Calculate target index based on accumulated drag offset from initial position
                                                val itemHeight = 44f
                                                val movedPositions =
                                                    (dragOffset / itemHeight).roundToInt()
                                                val targetIndex =
                                                    (initialDragIndex + movedPositions)
                                                        .coerceIn(0, layersList.lastIndex)

                                                if (targetIndex != currentDraggedIndex) {
                                                    val newList = layersList.toMutableList()
                                                    newList.removeAt(currentDraggedIndex)
                                                    newList.add(targetIndex, layerItem)
                                                    layersList = newList
                                                    draggedIndex = targetIndex
                                                }
                                            },
                                            onDragEnd = {
                                                draggedIndex = -1
                                                initialDragIndex = -1
                                                dragOffset = 0f
                                                onLayersReordered(layersList)
                                            },
                                            onDragCancel = {
                                                draggedIndex = -1
                                                initialDragIndex = -1
                                                dragOffset = 0f
                                            }
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Drag handle",
                                        tint = Color(0xFF9E9E9E),
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                }
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