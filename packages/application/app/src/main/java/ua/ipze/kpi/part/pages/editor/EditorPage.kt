package ua.ipze.kpi.part.pages.editor

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import ua.ipze.kpi.part.database.layer.Layer
import ua.ipze.kpi.part.pages.editor.fragments.BottomBar
import ua.ipze.kpi.part.pages.editor.fragments.ColorPaletteWithPicker
import ua.ipze.kpi.part.pages.editor.fragments.LayersPanel
import ua.ipze.kpi.part.pages.editor.fragments.MenuDialog
import ua.ipze.kpi.part.pages.editor.fragments.TopToolbar
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import ua.ipze.kpi.part.services.drawing.DrawCanvas
import ua.ipze.kpi.part.services.drawing.view.IDrawingViewModel
import ua.ipze.kpi.part.views.DatabaseProjectWithLayers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorPage(drawingViewModel: IDrawingViewModel, id: Long) {

    val data = BasePageDataProvider.current

    var projectData by remember { mutableStateOf<DatabaseProjectWithLayers?>(null) }

    var layers by remember { mutableStateOf<List<Layer>>(emptyList()) }

    var showMenu by remember { mutableStateOf(false) }
    var showColorPalette by remember { mutableStateOf(false) }
    var showLayers by remember { mutableStateOf(false) }
    var layerHidden by remember { mutableStateOf(false) }
    var selectedTool by remember { mutableStateOf(4) }
    var selectedColor by remember { mutableStateOf(Color(0xFFFFEB3B)) }
    var colors by remember { mutableStateOf<List<Color>>(emptyList()) }

    LaunchedEffect(id) {
        projectData = data.databaseViewModel.getProjectWithLayers(id)
    }

    // Use projectData here (will be null initially, then populate)
    projectData?.let { project ->
        layers = project.layers
        colors = project.project.palette.paletteList.map {
            Color(it.toULong())
        }
    }

//    LayersPanel(layerItems = layers)

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
                        drawingViewModel = drawingViewModel,
                        onLayersClick = { showLayers = !showLayers },
                        onEyeClick = { layerHidden = !layerHidden },
                        layerHidden, showLayers
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showColorPalette = !showColorPalette },
                    containerColor = Color(0xff777777),
                    modifier = Modifier
                        .border(width = 3.dp, color = Color.White)
                        .background(Color(0xff777777))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.color_selector_icon),
                        contentDescription = "Add",
                        tint = selectedColor
                    )
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
                DrawCanvas(Modifier.fillMaxSize(), drawingViewModel, { start: Offset, end: Offset ->
                    drawingViewModel.drawLine(start, end, selectedColor)
                })

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
                        contentDescription = null,
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
                    ColorPaletteWithPicker(
                        colors = colors,
                        selectedColor = selectedColor,
                        onColorSelected = { selectedColor = it },
                        onColorAdded = { newColor ->
                            colors = colors + newColor
                            Log.d("color", colors.size.toString())
                        },
                        onColorDeleted = { index ->
                            colors = colors.filterIndexed { i, _ -> i != index }
                        },
                        onCloseClick = { showColorPalette = false }
                    )
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
                projectData?.let { it ->
                    Column {
                        LayersPanel(
                            layerItems = layers,
                            width = it.project.width,
                            height = it.project.height,
                            onLayersReordered = {},
                            onLayerAdd = { layer -> layers = layers + layer },
                            onLayerDelete = { index ->
                                layers = layers.filterIndexed { i, _ -> i != index }
                            }
                        )

                        // Spacer to account for bottom bar
                        Spacer(modifier = Modifier.height(69.dp))
                    }
                }
            }
        }

        // Menu Dialog
        if (showMenu) {
            MenuDialog(onDismiss = {
                showMenu = false
                drawingViewModel.getOperativeData().start()
            })
            drawingViewModel.getOperativeData().pause()
        }
    }
}



