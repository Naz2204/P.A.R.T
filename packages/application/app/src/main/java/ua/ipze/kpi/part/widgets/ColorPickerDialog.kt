package ua.ipze.kpi.part.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import ua.ipze.kpi.part.ui.theme.pixelBorder
import ua.ipze.kpi.part.views.localizedStringResource

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