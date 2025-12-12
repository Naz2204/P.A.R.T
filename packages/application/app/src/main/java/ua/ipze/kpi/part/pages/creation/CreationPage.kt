package ua.ipze.kpi.part.pages.creation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.providers.languageChange.LanguageViewModel
import ua.ipze.kpi.part.providers.languageChange.localizedStringResource
import ua.ipze.kpi.part.ui.theme.pixelBorder
import ua.ipze.kpi.part.ui.theme.topBottomBorder
import ua.ipze.kpi.part.widgets.inputs.DropdownSelector

@Composable
fun CreationPage(languageViewModel: LanguageViewModel) {

    val possibleBackgrounds: List<Long> = listOf(0xffffffff, 0xffff0000, 0xff00ff00, 0xff0000ff)

    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var numLayers by remember { mutableStateOf("1") }
    var colorScheme by remember { mutableStateOf("") }
    var selectedBg by remember { mutableIntStateOf(0) }


    Column(modifier = Modifier.fillMaxSize()
        .background(Color(0xff232323))
        .padding(15.dp)
        .windowInsetsPadding(WindowInsets.navigationBars)
        .windowInsetsPadding(WindowInsets.ime)
        .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text(text = localizedStringResource(R.string.app_name, viewModel=languageViewModel), color = Color(0xffffffff),
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 20.sp)

        Text(text = localizedStringResource(R.string.new_art, viewModel=languageViewModel), fontSize = 30.sp, color = Color(0xffffffff),
            textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        Column() {
            Text(text = localizedStringResource(R.string.name, viewModel=languageViewModel), fontSize = 20.sp, color = Color(0xffffffff),
                modifier = Modifier.padding(start = 4.dp))
            BasicTextField(value = name, onValueChange = { name = it },
                modifier = Modifier
                    .pixelBorder(
                        backgroundColor = Color(0xff53565A),
                        borderWidth = 4.dp
                    )
                    .padding(4.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color(0xffffffff)))
        }
        Column() {
            Text(text = localizedStringResource(R.string.size, viewModel=languageViewModel), fontSize = 20.sp, color = Color(0xffffffff),
                modifier = Modifier.padding(start = 4.dp))
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
                    modifier = Modifier.padding(horizontal = 20.dp).size(15.dp))
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
            Text(text = localizedStringResource(R.string.init_color_scheme, viewModel=languageViewModel), fontSize = 20.sp, color = Color(0xffffffff),
                modifier = Modifier.padding(start = 4.dp))
            BasicTextField(value = colorScheme, onValueChange = { colorScheme = it },
                modifier = Modifier
                    .pixelBorder(
                        backgroundColor = Color(0xff53565A),
                        borderWidth = 4.dp
                    )
                    .padding(4.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color(0xffffffff)))
            Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                IconButton(onClick = {
                    TODO("Додати скан QR коду")
                }, modifier = Modifier.background(Color.LightGray).size(30.dp)) {
                    Image(
                        painter = painterResource(R.drawable.qr_code_icon),
                        contentDescription = null,
                        Modifier.background(Color.LightGray)
                    )
                }
                IconButton(onClick = {
                    TODO("Додати скан NFC")
                }, modifier = Modifier.background(Color.LightGray).size(30.dp)) {
                    Image(
                        painter = painterResource(R.drawable.nfc_icon),
                        contentDescription = null,
                    )
                }
            }
        }

        Column() {
            Text(text = localizedStringResource(R.string.init_layers_num, viewModel=languageViewModel), fontSize = 20.sp, color = Color(0xffffffff),
                modifier = Modifier.padding(start = 4.dp))
            BasicTextField(
                value = numLayers, onValueChange = { numLayers = it },
                modifier = Modifier
                    .pixelBorder(
                        backgroundColor = Color(0xff53565A),
                        borderWidth = 4.dp
                    )
                    .padding(4.dp)
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(color = Color(0xffffffff))
            )
        }

        Column() {
            Text(text = localizedStringResource(R.string.bg_fill, viewModel=languageViewModel), fontSize = 20.sp, color = Color(0xffffffff),
                modifier = Modifier.padding(start = 4.dp))
            DropdownSelector(possibleBackgrounds, selectedBg, { selectedBg = it },
                modifier = Modifier
                    .pixelBorder(
                        backgroundColor = Color(0xff53565A),
                        borderWidth = 4.dp
                    )
                    .padding(4.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(0.8f))
        Log.d("Recreate", "Recreated")
        Box(modifier = Modifier
            .topBottomBorder(4.dp, Color(0xffffffff), Color(0xff53565A))
            .clickable(onClick = {
                languageViewModel.setAppLanguage("en")
                Log.d("ButtonTest", "Button clicked")
//                TODO("Додати створення проєкту і перехід на сторінку редактора")
            })

        ) {
            Text(text = localizedStringResource(R.string.make, viewModel=languageViewModel), color = Color(0xffffffff),
                textAlign = TextAlign.Center, fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
    }
}