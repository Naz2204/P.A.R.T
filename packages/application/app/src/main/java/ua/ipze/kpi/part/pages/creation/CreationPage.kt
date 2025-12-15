package ua.ipze.kpi.part.pages.creation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.toColorLong
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.json.JSONObject
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.database.layer.Layer
import ua.ipze.kpi.part.database.project.LayersList
import ua.ipze.kpi.part.database.project.PaletteList
import ua.ipze.kpi.part.database.project.Project
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import ua.ipze.kpi.part.services.paletteApi.PaletteViewModel
import ua.ipze.kpi.part.services.qrWorker.QRDialog
import ua.ipze.kpi.part.services.qrWorker.ScanQrButton
import ua.ipze.kpi.part.services.qrWorker.jsonToColors
import ua.ipze.kpi.part.ui.theme.pixelBorder
import ua.ipze.kpi.part.ui.theme.topBottomBorder
import ua.ipze.kpi.part.views.localizedStringResource
import ua.ipze.kpi.part.widgets.inputs.DropdownSelector

@Composable
fun CreationPage() {

    val data = BasePageDataProvider.current
    val paletteViewModel: PaletteViewModel = viewModel()

    //TODO можливо замінити випадаюче меню на меню вибору кольору
    val possibleBackgrounds: List<Long> = listOf(0xffffffff, 0xffff0000, 0xff00ff00, 0xff0000ff)

    val scrollStateV = rememberScrollState()
    val scrollStateH = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var numLayers by remember { mutableStateOf("1") }
    var colorScheme by remember {
        mutableStateOf<List<List<Int>>>(
            listOf(
                listOf(255, 255, 255),
                listOf(255, 255, 255),
                listOf(255, 255, 255),
                listOf(255, 255, 255),
                listOf(255, 255, 255)
            )
        )
    }
    var selectedBg by remember { mutableIntStateOf(0) }
    var showQrDialog by remember { mutableStateOf(false) }

    if (showQrDialog) {
        QRDialog(colorScheme) {
            showQrDialog = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff232323))
            .padding(15.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .windowInsetsPadding(WindowInsets.ime)
            .verticalScroll(scrollStateV),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = localizedStringResource(R.string.app_name, data.language),
            color = Color(0xffffffff),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 20.sp
        )

        Text(
            text = localizedStringResource(R.string.new_art, data.language),
            fontSize = 30.sp,
            color = Color(0xffffffff),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Column() {
            Text(
                text = localizedStringResource(R.string.name, data.language),
                fontSize = 20.sp,
                color = Color(0xffffffff),
                modifier = Modifier.padding(start = 4.dp)
            )
            BasicTextField(
                value = name, onValueChange = { name = it },
                modifier = Modifier
                    .pixelBorder(
                        backgroundColor = Color(0xff53565A),
                        borderWidth = 4.dp
                    )
                    .padding(4.dp)
                    .fillMaxWidth(),
                textStyle = TextStyle(color = Color(0xffffffff))
            )
        }
        Column() {
            Text(
                text = localizedStringResource(R.string.size, data.language),
                fontSize = 20.sp,
                color = Color(0xffffffff),
                modifier = Modifier.padding(start = 4.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                Icon(
                    painter = painterResource(R.drawable.x_in_width), contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .size(15.dp)
                )
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
            Text(
                text = localizedStringResource(R.string.init_color_scheme, data.language),
                fontSize = 20.sp,
                color = Color(0xffffffff),
                modifier = Modifier.padding(start = 4.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                colorScheme.forEach { item ->
                    Box(
                        modifier = Modifier
                            .pixelBorder(backgroundColor = Color(0xffffffff), borderWidth = 4.dp)
                            .size(30.dp)
                            .background(
                                Color(
                                    red = item[0],
                                    green = item[1],
                                    blue = item[2]
                                )
                            )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .horizontalScroll(scrollStateH),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                ScanQrButton { qrJson ->
                    try {
                        val jsonArray = JSONObject(qrJson)
                        colorScheme = jsonToColors(jsonArray["colors"].toString())
                    } catch (t: Throwable) {
                    }
                }

                IconButton(
                    onClick = {
                        paletteViewModel.fetchColors({
                            colorScheme = it
                        })
                    }, modifier = Modifier
                        .background(Color.LightGray)
                        .size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.reload),
                        contentDescription = null,
                        tint = Color(0xff232323)
                    )
                }

                // Show QR Code button
                IconButton(
                    onClick = { showQrDialog = true },
                    modifier = Modifier
                        .background(Color.LightGray)
                        .size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.qr_code_icon),
                        contentDescription = null,
                        tint = Color(0xff232323)
                    )
                }
            }
        }

        Column() {
            Text(
                text = localizedStringResource(R.string.init_layers_num, data.language),
                fontSize = 20.sp,
                color = Color(0xffffffff),
                modifier = Modifier.padding(start = 4.dp)
            )
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
            Text(
                text = localizedStringResource(R.string.bg_fill, data.language),
                fontSize = 20.sp,
                color = Color(0xffffffff),
                modifier = Modifier.padding(start = 4.dp)
            )
            DropdownSelector(
                possibleBackgrounds, selectedBg, { selectedBg = it },
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
        Box(
            modifier = Modifier
                .topBottomBorder(4.dp, Color(0xffffffff), Color(0xff53565A))
                .clickable(onClick = {

                    val now = Clock.System.now()
                    val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
                    val epochTime = localDateTime.toInstant(TimeZone.currentSystemDefault())
                        .toEpochMilliseconds()

                    val listLayer = List(numLayers.toIntOrNull() ?: 1) {
                        Layer(
                            name = "",
                            visibility = true,
                            lock = false,
                            imageData = ByteArray(
                                ((width.toIntOrNull() ?: 1) * (height.toIntOrNull() ?: 1))
                            )
                        )
                    }
                    val project = Project(
                        layers = LayersList(emptyList()),
                        width = width.toIntOrNull() ?: 1,
                        height = height.toIntOrNull() ?: 1,
                        name = name,
                        //TODO додати геолокацію
                        lastGeolocation = "",
                        lastSettlement = "",
                        palette = PaletteList(colorScheme.map { it ->
                            Color(it[0], it[1], it[2]).toColorLong()
                        }),
                        timer = 0,
                        lastModified = epochTime,
                    )
                    data.databaseViewModel.saveProject(project, listLayer)
                })

        ) {
            Text(
                text = localizedStringResource(R.string.make, data.language),
                color = Color(0xffffffff),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
    }
}