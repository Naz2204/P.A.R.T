package ua.ipze.kpi.part.widgets.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Creates button with custom border
 * @param resourceId id of the text from resources to be placed inside button
 * @param start, [top], [end] and [bottom] Apply margin along each edge of the content in [Dp]
 * @param onClick Function to be executed after button clicked
 * @param image Switches button mode between text and image
 */
@Composable
fun GeneralButton(modifier: Modifier = Modifier, resourceId: Int, start: Dp = 0.dp, top: Dp = 0.dp, end: Dp = 0.dp,
                bottom: Dp = 0.dp, onClick:() -> Unit, image: Boolean = false) {
    Box(
        modifier = Modifier
            .padding(start, top, end, bottom)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (image) {
            Image(painterResource(resourceId), null, modifier = modifier)
        }
        else {
            Text(text = stringResource(resourceId), modifier = modifier)
        }
    }
}


/**
 * Creates button with custom border
 * @param resourceId id of the text from resources to be placed inside button
 * @param margin Margin of the button. Achieved by placing padding on outer box of the button. Calculated in [Dp]
 * @param onClick Function to be executed after button clicked
 * @param image Switches button mode between text and image
 */
@Composable
fun GeneralButton(modifier: Modifier = Modifier, resourceId: Int, margin: Dp = 0.dp, onClick:() -> Unit, image: Boolean = false) {
    GeneralButton(
        resourceId = resourceId,
        start = margin,
        top = margin,
        end = margin,
        bottom = margin,
        modifier = modifier,
        onClick = onClick,
        image = image,
    )
}