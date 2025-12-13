package ua.ipze.kpi.part.pages.login

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Build
import android.provider.Settings
import android.widget.ImageButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import ua.ipze.kpi.part.pages.login.fragments.PasswordInput
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import ua.ipze.kpi.part.router.GalleryPageData
import ua.ipze.kpi.part.ui.theme.topBottomBorder
import ua.ipze.kpi.part.utils.Biometry
import ua.ipze.kpi.part.utils.Biometry.*
import ua.ipze.kpi.part.views.PasswordViewModel
import ua.ipze.kpi.part.views.localizedStringResource

@Composable
fun LoginPage(passwordViewModel: PasswordViewModel, promptManager: Biometry) {
    var showError by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val data = BasePageDataProvider.current
    val biometricResult by promptManager.promptResult.collectAsState(initial = null)
    val addNewBiometricLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { }
    )


    Box (modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top) {
            Image(
                painter = painterResource(R.drawable.login_background_narrow),
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
                    .weight(0.35f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = localizedStringResource(R.string.app_name),
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
                    .weight(0.8f),
                color = Color(0xff53565a),
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
                        text = localizedStringResource(R.string.login),
                        color = Color(0xFFFEF3C7),
                        fontSize = 25.sp,
                        letterSpacing = 6.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PasswordInput(
                            label = localizedStringResource(R.string.password),
                            value = passwordViewModel.passwordInput,
                            onValueChange = {
                                passwordViewModel.passwordInput = it
                                showError = false
                            },
                            placeholder = "",
                            borderColor = if (showError) listOf(Color(0xffe53e2e), Color(0xff9e5f59))
                            else listOf(Color(0xffedb768), Color(0xff987d55))
                        )

                        Spacer(modifier = Modifier.height(50.dp))

                        Box(modifier = Modifier
                            .topBottomBorder(4.dp, Color(0xffedb768), Color.Transparent)
                            .clickable(onClick = {
                                passwordViewModel.handleLogin()
                                if (passwordViewModel.isAuthenticated) {
                                    showError = false
                                    data.nav.navigate(GalleryPageData)
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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(2.dp)
                                    .background(Color(0xffffffff))
                            )
                            Text(
                                text = localizedStringResource(R.string.or),
                                color = Color(0xFFCBD5E1),
                                fontSize = 20.sp,
                                modifier = Modifier.padding(horizontal = 18.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(2.dp)
                                    .background(Color(0xffffffff))
                            )
                        }


                        LaunchedEffect(biometricResult) {
                            if (biometricResult is BiometricResult.BiometryNotSet) {
                                if (Build.VERSION.SDK_INT >= 30) {
                                    val addNewBiometric = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                        putExtra(
                                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                                        )
                                    }
                                    addNewBiometricLauncher.launch(addNewBiometric)
                                }
                            }
                        }

                        val bioTitle = localizedStringResource(R.string.fingerprint_title)
                        val bioDesc = localizedStringResource(R.string.fingerprint_description)
                        IconButton(onClick = {
                            promptManager.showBiometricPrompt(title = bioTitle, description = bioDesc)
                            biometricResult?.let { result ->
                                when(result) {
                                    is BiometricResult.BiometryError,
                                    BiometricResult.BiometryNotSet,
                                    BiometricResult.FeatureUnavailable,
                                    BiometricResult.HardwareUnavailable,
                                    BiometricResult.NotOwner -> {
                                        showError = true
                                    }
                                    BiometricResult.Owner -> {
                                        showError = false
                                        data.nav.navigate(GalleryPageData)
                                    }
                                }
                            }
                        }, modifier = Modifier.background(Color.Transparent).size(120.dp)) {
                            Image(
                                painter = painterResource(R.drawable.fingerprint_icon),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}