package ua.ipze.kpi.part.widgets.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.res.painterResource
import ua.ipze.kpi.part.R

@Composable
fun DropdownSelector(
    items: List<String>,
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
            modifier = Modifier
                .clickable { expanded = true }
        ) {
            Text(text = items[selected], Modifier.background(color = Color(0xFFFFFF)))
            if (!expanded) {
                Icon(
                    painter = painterResource(R.drawable.dropdown_down_arrow),
                    contentDescription = null
                )
            }
            else {
                Icon(
                    painter = painterResource(R.drawable.dropdown_up_arrow),
                    contentDescription = null
                )
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {Text(text = item)},
                    onClick = {
                        onSelectedChange(index)
                        expanded = false
                    })
            }
        }
    }
}