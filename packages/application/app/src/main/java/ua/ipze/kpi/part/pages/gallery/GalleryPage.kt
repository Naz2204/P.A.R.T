package ua.ipze.kpi.part.pages.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.pages.gallery.fragments.ArtCard
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import ua.ipze.kpi.part.router.CreateArtPageData
import ua.ipze.kpi.part.router.EditorPageData
import ua.ipze.kpi.part.router.LoginPageData
import ua.ipze.kpi.part.views.PasswordViewModel
import ua.ipze.kpi.part.views.localizedStringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryPage(passwordViewModel: PasswordViewModel) {
    val data = BasePageDataProvider.current

//    var selectedTab by remember { mutableStateOf(0) }

    val artItems = data.databaseViewModel.getAllProjects().collectAsState(emptyList())

    val scope = rememberCoroutineScope()

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
                    text = localizedStringResource(R.string.app_name, data.language),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Text(
                    text = localizedStringResource(R.string.turtle, data.language),
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
                    modifier = Modifier
                        .weight(1f)
//                        .clickable(onClick = {
//                            selectedTab = 0
//                        })
                ) {
                    Icon(
                        painter = painterResource(R.drawable.image_frame_icon),
                        contentDescription = null,
//                        tint = if (selectedTab == 0) Color(0xFFE57373) else Color(0xffffffff),
                        tint = Color(0xffffffff),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        localizedStringResource(R.string.drawings, data.language),
//                        color = if (selectedTab == 0) Color(0xFFE57373) else Color(0xffffffff),
                        color = Color(0xffffffff),
                        fontSize = 12.sp
                    )
                }

//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier
//                        .weight(1f)
//                        .clickable(onClick = {
//                            selectedTab = 1
//                        })
//                ) {
//                    Icon(
//                        painter = painterResource(R.drawable.app_theams_icon),
//                        contentDescription = null,
//                        tint = if (selectedTab == 1) Color(0xFFE57373) else Color(0xffffffff),
//                        modifier = Modifier.size(24.dp)
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        localizedStringResource(R.string.app_themes, data.language),
//                        color = if (selectedTab == 1) Color(0xFFE57373) else Color(0xffffffff),
//                        fontSize = 12.sp
//                    )
//                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = {
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
                        localizedStringResource(R.string.change_key, data.language),
                        color = Color(0xffffffff),
                        fontSize = 12.sp
                    )
                }
            }

            Box(
                modifier = Modifier
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
                items(artItems.value) { item ->
                    ArtCard(
                        item = item,
                        onDeleteClick = { id ->
                            scope.launch {
                                data.databaseViewModel.deleteProject(id)
                            }
                        },
                        onNameChange = { id, newName ->
                            scope.launch {
                                data.databaseViewModel.renameProject(id, newName)
                            }
                        },
                        onCardClick = { id ->
                            data.nav.navigate(
                                EditorPageData(
                                    historyLength = 0,
                                    id = id
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
