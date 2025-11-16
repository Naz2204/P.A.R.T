package ua.ipze.kpi.part
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import ua.ipze.kpi.part.router.AppRouter
import ua.ipze.kpi.part.ui.theme.PARTTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            PARTTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppRouter(innerPadding, {context: Context, language: String -> setLanguage(context, language)})
                }
            }
        }
    }

    fun setLanguage(context: Context, language: String) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            context.getSystemService(LocaleManager::class.java)
//                .applicationLocales = LocaleList.forLanguageTags(language)
//        }
//        else {
//            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
//        }
//        saveLanguage(context, language)
    }
}
