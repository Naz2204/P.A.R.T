package ua.ipze.kpi.part.pages.gallery.fragments

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.database.project.Project
import ua.ipze.kpi.part.ui.theme.pixelBorder


@Composable
fun ArtCard(
    item: Project,
    onDeleteClick: (Long) -> Unit,
    onNameChange: (Long, String) -> Unit,
    onCardClick: (Long) -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf(item.name) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.68f),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2B2B2B)
        ),
        shape = RoundedCornerShape(0.dp),
        onClick = { if (!showMenu) onCardClick(item.id) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(4.dp)
            ) {
                // Replace with actual image loading (Coil or Glide)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    val bmp = BitmapFactory.decodeByteArray(
                        item.previewImageData,
                        0,
                        item.previewImageData.size
                    )
                    bmp?.let {
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = null,
                            filterQuality = FilterQuality.None,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }
            }

            // Dimensions
            Text(
                text = "${item.width}x${item.height}",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 8.dp, bottom = 4.dp),
                color = Color.White,
                fontSize = 11.sp
            )

            // Bottom section
            if (!showMenu) Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 4.dp, bottom = 8.dp, top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.name,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = item.lastSettlement,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                }
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            else Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 4.dp, bottom = 8.dp, top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                    modifier = Modifier
                        .weight(1f)
                        .pixelBorder(borderWidth = 1.5.dp)
                )

                IconButton(
                    onClick = { onDeleteClick(item.id) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.trash_bin),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = {
                        showMenu = false
                        if (name != item.name) {
                            onNameChange(item.id, name)
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(if (name == item.name) R.drawable.close_x_icon else R.drawable.check),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

