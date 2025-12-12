package ua.ipze.kpi.part.pages.start

import androidx.compose.animation.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.ipze.kpi.part.providers.languageChange.localizedStringResource
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.ui.theme.pixelBorder

@Composable
fun StartPage() {

    var name by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var numLayers by remember { mutableStateOf("") }
    var colorScheme by remember {mutableStateOf("")}

    Column(modifier = Modifier.fillMaxSize().background(Color(0xff232323)).padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(text = localizedStringResource(R.string.app_name), color = Color(0xffffffff),
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 20.sp)

        Text(text = localizedStringResource(R.string.new_art), fontSize = 30.sp, color = Color(0xffffffff),
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        Column() {
            Text(text = localizedStringResource(R.string.name), fontSize = 20.sp, color = Color(0xffffffff))
            BasicTextField(value = name, onValueChange = { name = it },
                modifier = Modifier
                    .pixelBorder(
                        backgroundColor = Color(0xff53565A),
                        borderWidth = 4.dp
                    )
                    .padding(4.dp),
                textStyle = TextStyle(color = Color(0xffffffff)))
        }
        Column() {
            Text(text = localizedStringResource(R.string.size), fontSize = 20.sp, color = Color(0xffffffff))
            Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = width, onValueChange = { width = it },
                    modifier = Modifier
                        .pixelBorder(
                            backgroundColor = Color(0xff53565A),
                            borderWidth = 4.dp
                        )
                        .padding(4.dp)
                        .weight(0.2f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(color = Color(0xffffffff))
                )
                Image(painter = painterResource(R.drawable.x_in_width), contentDescription = null,
                    modifier = Modifier.padding(horizontal = 20.dp))
                BasicTextField(
                    value = height, onValueChange = { height = it },
                    modifier = Modifier
                        .pixelBorder(
                            backgroundColor = Color(0xff53565A),
                            borderWidth = 4.dp
                        )
                        .padding(4.dp)
                        .weight(0.2f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = TextStyle(color = Color(0xffffffff))
                )
            }
        }

        Column() {
            Text(text = localizedStringResource(R.string.init_color_scheme), fontSize = 20.sp, color = Color(0xffffffff))
            BasicTextField(value = colorScheme, onValueChange = { colorScheme = it },
                modifier = Modifier
                    .pixelBorder(
                        backgroundColor = Color(0xff53565A),
                        borderWidth = 4.dp
                    )
                    .padding(4.dp),
                textStyle = TextStyle(color = Color(0xffffffff)))
        }

        Column() {
            Text(text = localizedStringResource(R.string.init_layers_num), fontSize = 20.sp, color = Color(0xffffffff))
            BasicTextField(
                value = numLayers, onValueChange = { numLayers = it },
                modifier = Modifier
                    .pixelBorder(
                        backgroundColor = Color(0xff53565A),
                        borderWidth = 4.dp
                    )
                    .padding(4.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(color = Color(0xffffffff))
            )
        }
    }
}