package ua.ipze.kpi.part.pages.editor

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.pages.editor.fragments.BottomBar
import ua.ipze.kpi.part.pages.editor.fragments.ColorPalette
import ua.ipze.kpi.part.pages.editor.fragments.LayersPanel
import ua.ipze.kpi.part.pages.editor.fragments.MenuDialog
import ua.ipze.kpi.part.pages.editor.fragments.TopToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawingPage() {
    var showMenu by remember { mutableStateOf(false) }
    var showColorPalette by remember { mutableStateOf(false) }
    var showLayers by remember { mutableStateOf(false) }
    var layerHidden by remember { mutableStateOf(false) }
    var selectedTool by remember { mutableStateOf(0) }
    var selectedColor by remember { mutableStateOf(Color(0xFFFFEB3B)) }

    val colors = listOf(
        Color.White, Color.Black,
        Color(0xFFD32F2F), Color(0xFF8B0000),
        Color(0xFFFFC107), Color.Gray,
        Color(0xFFFFEB3B)
    )

    val layers = remember { mutableStateListOf("Layer 0", "Layer 1", "Layer 2", "Layer 3") }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets(0, 0, 0, 0)),
            topBar = {
                TopToolbar(
                    selectedTool = selectedTool,
                    onToolSelected = { selectedTool = it },
                    onMenuClick = { showMenu = true },
                    onUndoClick = { TODO("Додати виклик undo") },
                    onRedoClick = { TODO("Додати виклик redo") }
                )
            },
            bottomBar = {
                Column {
                    BottomBar(
                        onLayersClick = { showLayers = !showLayers },
                        onEyeClick = { layerHidden = !layerHidden },
                        layerHidden, showLayers,
                        "Change to actual size"
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showColorPalette = !showColorPalette }) {
                    Icon(painter = painterResource(R.drawable.color_selector_icon), contentDescription = "Add",
                        tint = selectedColor)
                }
            },
            containerColor = Color(0xFF424242),
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFF616161))
            ) {
                PixelCanvas(modifier = Modifier.fillMaxSize())

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .size(48.dp)
                        .background(Color(0xFF757575), RoundedCornerShape(4.dp))
                        .border(2.dp, Color.White, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Tool",
                        tint = selectedColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            // Color Palette Panel - overlays on top

        }
        if (showColorPalette) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Column {
                    ColorPalette(
                        colors = colors,
                        selectedColor = selectedColor,
                        onColorSelected = { selectedColor = it }
                    )

                    // Spacer to account for bottom bar
                    Spacer(modifier = Modifier.height(69.dp))
                }
            }
        }

        // Layers Panel - overlays on top
        if (showLayers) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                Column {
                    LayersPanel(layers = layers)

                    // Spacer to account for bottom bar
                    Spacer(modifier = Modifier.height(69.dp))
                }
            }
        }
        // Menu Dialog
        if (showMenu) {
            MenuDialog(onDismiss = { showMenu = false })
        }
    }
}



@Composable
fun PixelCanvas(modifier: Modifier = Modifier) {
    val gridSize = 10
    val cellsPerRow = 18
    val cellsPerColumn = 70

    Canvas(modifier = modifier) {
        val cellWidth = size.width / cellsPerRow
        val cellHeight = size.height / cellsPerColumn

        // Draw grid
        for (i in 0..cellsPerRow) {
            drawLine(
                color = Color(0xFF757575),
                start = Offset(i * cellWidth, 0f),
                end = Offset(i * cellWidth, size.height),
                strokeWidth = 1f
            )
        }

        for (i in 0..cellsPerColumn) {
            drawLine(
                color = Color(0xFF757575),
                start = Offset(0f, i * cellHeight),
                end = Offset(size.width, i * cellHeight),
                strokeWidth = 1f
            )
        }

        // Draw some example pixels (checkerboard pattern)
        for (row in 0 until cellsPerColumn) {
            for (col in 0 until cellsPerRow) {
                val isGray = (row + col) % 2 == 0
                if (isGray) {
                    drawRect(
                        color = Color(0xFF9E9E9E),
                        topLeft = Offset(col * cellWidth, row * cellHeight),
                        size = androidx.compose.ui.geometry.Size(cellWidth, cellHeight)
                    )
                } else {
                    drawRect(
                        color = Color(0xFFBDBDBD),
                        topLeft = Offset(col * cellWidth, row * cellHeight),
                        size = androidx.compose.ui.geometry.Size(cellWidth, cellHeight)
                    )
                }
            }
        }
    }
}
