package ua.ipze.kpi.part.pages.editor.fragments

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import ua.ipze.kpi.part.services.qrWorker.QRDialog
import ua.ipze.kpi.part.ui.theme.pixelBorder
import ua.ipze.kpi.part.views.localizedStringResource

@Composable
fun ColorPaletteWithPicker(
    colors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    onColorAdded: (Color) -> Unit,
    onColorDeleted: (index: Int) -> Unit,
    onCloseClick: () -> Unit
) {
    var showColorPicker by remember { mutableStateOf(false) }

    Log.d("red", "redrown + ${colors.size}")
    ColorPalette(
        colors = colors,
        selectedColor = selectedColor,
        onColorSelected = onColorSelected,
        onAddClick = { showColorPicker = true },
        onCopyClick = onColorAdded,
        onDeleteClick = onColorDeleted,
        onCloseClick = onCloseClick
    )

    if (showColorPicker) {
        ColorPickerDialog(
            onDismiss = { showColorPicker = false },
            onColorSelected = { color ->
                onColorAdded(color)
                showColorPicker = false
            }
        )
    }
}

@Composable
fun ColorPalette(
    colors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    onAddClick: () -> Unit,
    onCopyClick: (color: Color) -> Unit,
    onDeleteClick: (index: Int) -> Unit,
    onCloseClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedIndex by remember { mutableStateOf(0) }
    var showQrDialog by remember { mutableStateOf(false) }

    if (showQrDialog) {
        QRDialog(colors.map { color ->
            listOf(
                (color.red * 255).toInt(),
                (color.green * 255).toInt(),
                (color.blue * 255).toInt()
            )
        }
        ) {
            showQrDialog = false
        }
    }

    Surface(
        color = Color(0xff60646a),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Action buttons
            Log.d("red", "redrown2")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = onAddClick) {
                    Icon(
                        painter = painterResource(R.drawable.add_button_icon),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                IconButton(onClick = {
                    onCopyClick(colors[selectedIndex])
                }) {
                    Icon(
                        painter = painterResource(R.drawable.copy_button_icon),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                IconButton(onClick = { onDeleteClick(selectedIndex) }) {
                    Icon(
                        painter = painterResource(R.drawable.trash_bin),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                IconButton(onClick = { showQrDialog = true }) {
                    Icon(
                        painter = painterResource(R.drawable.share),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                IconButton(onClick = onCloseClick) {
                    Icon(
                        painter = painterResource(R.drawable.menu_arrow_down),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            HorizontalDivider(
                color = Color(0xFF757575),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Color swatches
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.Center
            ) {
                colors.forEachIndexed { index, color ->
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(4.dp)
                            .background(color, RoundedCornerShape(4.dp))
                            .border(
                                width = if (index == selectedIndex) 3.dp else 1.dp,
                                color = if (index == selectedIndex) Color.White else Color(
                                    0xFF757575
                                ),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                onColorSelected(color)
                                selectedIndex = index
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun ColorPickerDialog(
    onDismiss: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    var red by remember { mutableFloatStateOf(0.5f) }
    var green by remember { mutableFloatStateOf(0.5f) }
    var blue by remember { mutableFloatStateOf(0.5f) }

    val data = BasePageDataProvider.current
    val currentColor = Color(red, green, blue)

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.background(color = Color.LightGray), containerColor = Color.LightGray,
        title = { Text(text = localizedStringResource(R.string.color, data.language)) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Color preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .pixelBorder(borderWidth = 6.dp, backgroundColor = currentColor)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Red slider
                Text(
                    text = "Red: ${(red * 255).toInt()}",
                    color = Color.Red
                )
                Slider(
                    value = red,
                    onValueChange = { red = it },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Red,
                        activeTrackColor = Color.Red
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Green slider
                Text(
                    text = "Green: ${(green * 255).toInt()}",
                    color = Color.Green
                )
                Slider(
                    value = green,
                    onValueChange = { green = it },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Green,
                        activeTrackColor = Color.Green
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Blue slider
                Text(
                    text = "Blue: ${(blue * 255).toInt()}",
                    color = Color.Blue
                )
                Slider(
                    value = blue,
                    onValueChange = { blue = it },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Blue,
                        activeTrackColor = Color.Blue
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Hex value
                Text(
                    text = "Hex: #ff${colorToHex(currentColor)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onColorSelected(currentColor) }) {
                Text(
                    text = localizedStringResource(R.string.add, data.language),
                    color = Color(0xff232323)
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = localizedStringResource(R.string.cancel, data.language),
                    color = Color(0xff232323)
                )
            }
        }
    )
}

fun colorToHex(color: Color): String {
    val red = (color.red * 255).toInt()
    val green = (color.green * 255).toInt()
    val blue = (color.blue * 255).toInt()
    return "%02X%02X%02X".format(red, green, blue)
}