package ua.ipze.kpi.part.pages.editor.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.ui.theme.pixelBorder

@Composable
fun TopToolbar(
    selectedTool: Int,
    onToolSelected: (Int) -> Unit,
    onMenuClick: () -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        val tools = listOf(
            R.drawable.undo_tool_icon,
            R.drawable.redo_tool_icon,
            R.drawable.settings_icon,
            R.drawable.pen_tool_icon,
            R.drawable.eraser_tool_icon,
            R.drawable.color_picker_tool_icon
        )
//        Row(
//            horizontalArrangement = Arrangement.SpaceEvenly,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            IconButton(
//                onClick = {
//                    onUndoClick()
//                },
//                modifier = Modifier.size(48.dp)
//            ) {
//                Icon(
//                    painter = painterResource(tools[0]),
//                    contentDescription = null,
//                    tint = Color(0xffffffff),
//                    modifier = Modifier
//                        .size(35.dp)
//                        .pixelBorder(borderWidth = 2.dp)
//                        .padding(2.dp)
//                )
//            }
//            IconButton(
//                onClick = {
//                    onRedoClick()
//                },
//                modifier = Modifier.size(48.dp)
//            ) {
//                Icon(
//                    painter = painterResource(tools[1]),
//                    contentDescription = null,
//                    tint = Color(0xffffffff),
//                    modifier = Modifier
//                        .size(35.dp)
//                        .pixelBorder(borderWidth = 2.dp)
//                        .padding(2.dp)
//                )
//            }
//            IconButton(
//                onClick = {
//                    onMenuClick()
//                },
//                modifier = Modifier.size(48.dp)
//            ) {
//                Icon(
//                    painter = painterResource(tools[2]),
//                    contentDescription = null,
//                    tint = Color(0xffffffff),
//                    modifier = Modifier
//                        .size(35.dp)
//                        .pixelBorder(borderWidth = 2.dp)
//                        .padding(2.dp)
//                )
//            }
//        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    onToolSelected(3)
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(tools[3]),
                    contentDescription = null,
                    tint = Color(0xffffffff),
                    modifier = Modifier
                        .size(35.dp)
                        .pixelBorder(
                            borderWidth = 2.dp,
                            innerColor = if (selectedTool == 3) Color(0xff903d39)
                            else Color.LightGray
                        )
                        .padding(2.dp)
                )
            }
            IconButton(
                onClick = {
                    onToolSelected(4)
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(tools[4]),
                    contentDescription = null,
                    tint = Color(0xffffffff),
                    modifier = Modifier
                        .size(35.dp)
                        .pixelBorder(
                            borderWidth = 2.dp,
                            innerColor = if (selectedTool == 4) Color(0xff903d39)
                            else Color.LightGray
                        )
                        .padding(2.dp)
                )
            }
            IconButton(
                onClick = {
                    onToolSelected(5)
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(tools[5]),
                    contentDescription = null,
                    tint = Color(0xffffffff),
                    modifier = Modifier
                        .size(35.dp)
                        .pixelBorder(
                            borderWidth = 2.dp,
                            innerColor = if (selectedTool == 5) Color(0xff903d39)
                            else Color.LightGray
                        )
                        .padding(2.dp)
                )
            }
            IconButton(
                onClick = {
                    onMenuClick()
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(tools[2]),
                    contentDescription = null,
                    tint = Color(0xffffffff),
                    modifier = Modifier
                        .size(35.dp)
                        .pixelBorder(borderWidth = 2.dp)
                        .padding(2.dp)
                )
            }
        }
    }
}