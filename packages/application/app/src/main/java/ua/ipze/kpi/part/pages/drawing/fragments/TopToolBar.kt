package ua.ipze.kpi.part.pages.drawing.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TopToolbar(
    selectedTool: Int,
    onToolSelected: (Int) -> Unit,
    onMenuClick: () -> Unit
) {
    Surface(
        color = Color(0xFF303030),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val tools = listOf(
                Icons.Default.Refresh,
                Icons.Default.Add,
                Icons.Default.List,
                Icons.Default.Settings,
                Icons.Default.Edit,
                Icons.Default.Create,
                Icons.Default.AccountBox,
                Icons.Default.Clear
            )

            tools.forEachIndexed { index, icon ->
                IconButton(
                    onClick = {
                        if (index == 3) onMenuClick()
                        else onToolSelected(index)
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (selectedTool == index) Color(0xFF424242) else Color.Transparent,
                            RoundedCornerShape(4.dp)
                        )
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Tool $index",
                        tint = if (index == 0 || index == 4) Color(0xFFD32F2F) else Color(0xFF9E9E9E)
                    )
                }
            }
        }
    }
}