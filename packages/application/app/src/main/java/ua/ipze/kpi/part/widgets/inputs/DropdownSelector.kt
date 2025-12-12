package ua.ipze.kpi.part.widgets.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.ipze.kpi.part.R
@OptIn(ExperimentalStdlibApi::class)
@Composable
fun DropdownSelector(
    items: List<Long>,
    selected: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box (
        modifier = modifier
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { expanded = true },
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = items[selected].toHexString(HexFormat {
                upperCase = false
                number.prefix = "#"
                number.minLength = 8
                number.removeLeadingZeros = true
            }), Modifier.background(color = Color(items[selected])).weight(0.5f),
                fontSize = 20.sp, textAlign = TextAlign.Center)
            Icon(
                painter = painterResource(if(expanded) R.drawable.dropdown_up_arrow else R.drawable.dropdown_down_arrow),
                contentDescription = null,
                tint = Color(0xffffffff)
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(color = Color(0xff53565A))
                .width(300.dp)
                .padding(horizontal = 5.dp),
            shape = (RoundedCornerShape(0.dp))
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {Text(text = item.toHexString(HexFormat {
                        upperCase = false
                        number.prefix = "#"
                        number.minLength = 8
                        number.removeLeadingZeros = true
                    }), textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.pixel_font_7)),
                        modifier = Modifier.fillMaxWidth()
                    )},
                    onClick = {
                        onSelectedChange(index)
                        expanded = false
                    },
                    modifier = Modifier.background(color = Color(item))
                        .border(2.dp, Color(0xff53565A))
                        .height(22.dp)
                )
            }
        }
    }
}