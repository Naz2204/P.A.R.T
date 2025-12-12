package ua.ipze.kpi.part
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.ipze.kpi.part.views.LanguageViewModel
import ua.ipze.kpi.part.router.AppRouter
import ua.ipze.kpi.part.ui.theme.PARTTheme
import ua.ipze.kpi.part.views.PasswordViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        enableEdgeToEdge()
        setContent {
            val languageViewModel: LanguageViewModel = viewModel()
            val passwordViewModel: PasswordViewModel = viewModel()

            PARTTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppRouter(innerPadding, languageViewModel, passwordViewModel)
                }
            }
        }
    }
}
