//package ua.ipze.kpi.part.pages.login
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.content.ContextCompat.getString
//import ua.ipze.kpi.part.R
//
//@Composable
//fun PasswordPage() {
//    var password by remember { mutableStateOf("") }
//    var repeatPassword by remember { mutableStateOf("") }
//    var savedPassword by remember { mutableStateOf("") }
//    var showError by remember { mutableStateOf(false) }
//
//    Box (modifier = Modifier.fillMaxSize()) {
//        Column(modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Top) {
//            Image(
//                painter = painterResource(R.drawable.login_background),
//                contentDescription = null,
//                modifier = Modifier.fillMaxWidth(),
//                contentScale = ContentScale.Crop
//            )
//        }
//
//
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Top section with logo and pixel art background (takes ~35% of screen)
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(0.5f),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "P>A.R.T",
//                    fontSize = 62.sp,
//                    color = Color(0xffffffff),
//                    letterSpacing = 10.sp
//                )
//            }
//
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(0.65f),
//                color = Color(0xFF4A5568),
//                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 48.dp)
//                        .padding(top = 25.dp, bottom = 32.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "LOGIN",
//                        color = Color(0xFFFEF3C7),
//                        fontSize = 25.sp,
//                        letterSpacing = 6.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(bottom = 15.dp)
//                    )
//
//                    when (screenType) {
//                        ScreenType.CREATE -> CreatePasswordScreen(
//                            password = password,
//                            repeatPassword = repeatPassword,
//                            showError = showError,
//                            onPasswordChange = {
//                                password = it
//                                showError = false
//                            },
//                            onRepeatPasswordChange = {
//                                repeatPassword = it
//                                showError = false
//                            },
//                            onRun = {
//                                if (password == repeatPassword && password.isNotEmpty()) {
//                                    savedPassword = password
//                                    password = ""
//                                    repeatPassword = ""
//                                    showError = false
//                                    screenType = ScreenType.LOGIN
//                                } else {
//                                    showError = true
//                                }
//                            }
//                        )
//                        ScreenType.LOGIN -> LoginScreen(
//                            password = password,
//                            showError = showError,
//                            onPasswordChange = {
//                                password = it
//                                showError = false
//                            },
//                            onRun = {
//                                if (password == savedPassword) {
//                                    showError = false
//                                } else {
//                                    showError = true
//                                }
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun LoginScreen(
//    password: String,
//    showError: Boolean,
//    onPasswordChange: (String) -> Unit,
//    onRun: () -> Unit
//) {
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Password field
//        PasswordInputField(
//            label = "Password",
//            value = password,
//            onValueChange = onPasswordChange,
//            placeholder = "",
//            borderColor = Color(0xFFEA923A)
//        )
//
//        Spacer(modifier = Modifier.height(64.dp))
//
//        // RUN button
//        RunButton(onClick = onRun)
//
//        Spacer(modifier = Modifier.height(40.dp))
//
//        // Divider with "or"
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 32.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(1.dp)
//                    .background(Color(0xFF94A3B8))
//            )
//            Text(
//                text = "or",
//                style = TextStyle(
//                    color = Color(0xFFCBD5E1),
//                    fontSize = 14.sp
//                ),
//                modifier = Modifier.padding(horizontal = 20.dp)
//            )
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(1.dp)
//                    .background(Color(0xFF94A3B8))
//            )
//        }
//
//        // Fingerprint icon
//        Box(
//            modifier = Modifier
//                .size(80.dp)
//                .border(4.dp, Color(0xFFEA923A), RoundedCornerShape(6.dp))
//                .background(Color(0xFF334155), RoundedCornerShape(6.dp)),
//            contentAlignment = Alignment.Center
//        ) {
//            // Fingerprint representation
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier.padding(8.dp)
//            ) {
//                repeat(5) { index ->
//                    Box(
//                        modifier = Modifier
//                            .width((40 - index * 7).dp)
//                            .height(3.dp)
//                            .background(Color(0xFFEA923A))
//                    )
//                    if (index < 4) {
//                        Spacer(modifier = Modifier.height(3.dp))
//                    }
//                }
//            }
//        }
//    }
//}
//
//
//
//@Composable
//fun RunButton(onClick: () -> Unit) {
//    Surface(
//        onClick = onClick,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(64.dp)
//            .border(4.dp, Color(0xFFB45309), RoundedCornerShape(6.dp)),
//        color = Color(0xFFEA923A),
//        shape = RoundedCornerShape(6.dp)
//    ) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = "RUN",
//                style = TextStyle(
//                    fontSize = 32.sp,
//                    letterSpacing = 14.sp,
//                    color = Color.White
//                )
//            )
//        }
//    }
//}