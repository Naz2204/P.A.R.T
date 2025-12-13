package ua.ipze.kpi.part.pages.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.pages.gallery.fragments.ArtCard
import ua.ipze.kpi.part.pages.login.PasswordCreationPage
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import ua.ipze.kpi.part.router.CreateArtPageData
import ua.ipze.kpi.part.router.LoginPageData
import ua.ipze.kpi.part.views.PasswordViewModel
import ua.ipze.kpi.part.views.localizedStringResource

data class ArtItem(
    val id: Int,
    val imageRes: Int,
    val title: String,
    val location: String,
    val dimensions: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryPage(
//    onNavigateToEditor: (Int) -> Unit = {}
    passwordViewModel: PasswordViewModel
) {
    val data = BasePageDataProvider.current

    var selectedTab by remember { mutableStateOf(0) }

    val artItems = List(8) { index ->
        ArtItem(
            id = index,
            imageRes = R.drawable.ic_launcher_background, // Replace with actual resource
            title = "Ko-te-cu",
            location = "Lviv, Ukraine",
            dimensions = "500x1250"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = localizedStringResource(R.string.app_name),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Text(
                    text = "for real turteles",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // Navigation Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f).clickable(onClick = {
                        selectedTab = 0
                    })
                ) {
                    Icon(
                        painter = painterResource(R.drawable.image_frame_icon),
                        contentDescription = null,
                        tint = if (selectedTab == 0) Color(0xFFE57373) else Color(0xffffffff),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        localizedStringResource(R.string.drawings),
                        color = if (selectedTab == 0) Color(0xFFE57373) else Color(0xffffffff),
                        fontSize = 12.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f).clickable(onClick = {
                        selectedTab = 1
                    })
                ) {
                    Icon(
                        painter = painterResource(R.drawable.app_theams_icon),
                        contentDescription = null,
                        tint = if (selectedTab == 1) Color(0xFFE57373) else Color(0xffffffff),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        localizedStringResource(R.string.app_themes),
                        color = if (selectedTab == 1) Color(0xFFE57373) else Color(0xffffffff),
                        fontSize = 12.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f).clickable(onClick = {
                        passwordViewModel.clearPassword()
                        data.nav.navigate(LoginPageData)
                    })
                ) {
                    Icon(
                        painter = painterResource(R.drawable.key_icon),
                        contentDescription = null,
                        tint = Color(0xffffffff),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        localizedStringResource(R.string.change_key),
                        color = Color(0xffffffff),
                        fontSize = 12.sp
                    )
                }
            }

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Color(0xffffffff))
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        data.nav.navigate(CreateArtPageData)
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_button_icon),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xffffffff)
                    )
                }
            }

            // Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(artItems) { item ->
                    ArtCard(
                        item = item,
                        onCardClick = { clickedItem ->
//                            onNavigateToEditor(clickedItem.id)
                        }
                    )
                }
            }
        }
    }
}
