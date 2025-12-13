package ua.ipze.kpi.part.pages.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import ua.ipze.kpi.part.router.CreateArtPageData
import ua.ipze.kpi.part.views.PasswordViewModel
import ua.ipze.kpi.part.views.localizedStringResource
import ua.ipze.kpi.part.pages.login.fragments.PasswordInput
import ua.ipze.kpi.part.router.GalleryPageData
import ua.ipze.kpi.part.router.LoginPageData
import ua.ipze.kpi.part.ui.theme.topBottomBorder

@Composable
fun PasswordCreationPage(passwordViewModel: PasswordViewModel) {
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    var showError by remember { mutableStateOf(false) }

    val data = BasePageDataProvider.current
    Box (modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top) {
            Image(
                painter = painterResource(R.drawable.login_background),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.3f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "P>A.R.T",
                    fontSize = 62.sp,
                    color = Color(0xffffffff),
                    letterSpacing = 10.sp
                )
            }
            Surface(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .windowInsetsPadding(WindowInsets.ime)
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
                    .weight(0.65f),
                color = Color(0xFF4A5568),
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 48.dp)
                        .padding(top = 25.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = localizedStringResource(R.string.make_password),
                        color = Color(0xFFFEF3C7),
                        fontSize = 25.sp,
                        letterSpacing = 6.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PasswordInput(
                            label = localizedStringResource(R.string.password),
                            value = password,
                            onValueChange = {password = it},
                            placeholder = "",
                            borderColor = if (showError) listOf(Color(0xffe53e2e), Color(0xff9e5f59))
                            else listOf(Color(0xffedb768), Color(0xff987d55))
                        )

                        Spacer(modifier = Modifier.height(40.dp))

                        PasswordInput(
                            label = localizedStringResource(R.string.repeat),
                            value = repeatPassword,
                            onValueChange = {repeatPassword = it},
                            placeholder = "",
                            borderColor = if (showError) listOf(Color(0xffe53e2e), Color(0xff9e5f59))
                            else listOf(Color(0xffedb768), Color(0xff987d55))
                        )

                        Spacer(modifier = Modifier.height(64.dp))

                        Box(modifier = Modifier
                            .topBottomBorder(4.dp, Color(0xffedb768), Color.Transparent)
                            .clickable(onClick = {
                                if (password == repeatPassword) {
                                    showError = false
                                    passwordViewModel.passwordInput = password
                                    passwordViewModel.createPass()
                                } else {
                                    showError = true
                                }
                            })
                            .background(brush = Brush.linearGradient(
                                colors = listOf(Color(0x03edb768), Color(0x99edb768), Color(0x03edb768))
                            ))
                        ) {
                            Text(text = localizedStringResource(R.string.run), color = Color(0xffffffff),
                                textAlign = TextAlign.Center, fontSize = 30.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                            )
                        }
                    }
            }
        }
    }
}
}