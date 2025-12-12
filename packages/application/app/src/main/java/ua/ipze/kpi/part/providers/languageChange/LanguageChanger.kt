package ua.ipze.kpi.part.providers.languageChange

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

// --- STATE MANAGEMENT ---

data class AppLocaleState(
    val currentLocale: Locale = Locale.getDefault()
)

class LanguageViewModel : ViewModel() {
    var localeState by mutableStateOf(getPersistedLocale())
        private set

    fun setAppLanguage(languageCode: String) {
        val newLocale = Locale.forLanguageTag(languageCode)
        Log.d("CurrentLocale", "New locale $newLocale")
        localeState = localeState.copy(currentLocale = newLocale)
        // TODO: Persist the 'languageCode' here using DataStore/SharedPreferences
    }

    private fun getPersistedLocale(): AppLocaleState {
        // TODO: Replace with your actual storage retrieval logic.
        return AppLocaleState(Locale.getDefault())
    }
}

// --- LOCALIZATION UTILITY ---

fun getLocalizedContext(context: Context, locale: Locale): Context {
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    val localeList = android.os.LocaleList(locale)
    config.setLocales(localeList)
    return  context.createConfigurationContext(config)
}


// --- THE CUSTOM STRING RESOURCE COMPOSABLE ---

@Composable
fun localizedStringResource(
    @StringRes id: Int,
    vararg formatArgs: Any,
    viewModel: LanguageViewModel = viewModel()
): String {
    val context = LocalContext.current
    val currentLocale = viewModel.localeState.currentLocale
    Log.d("CurrentLocale", "Current locale $currentLocale")
    val localizedContext = remember(currentLocale) {
        getLocalizedContext(context, currentLocale)
    }

    return localizedContext.resources.getString(id, *formatArgs)
}

//  TODO прибрати цей коментар перед завершенням розробки
// --- EXAMPLE USAGE COMPOSABLE ---

//@Composable
//fun LanguageSwitcherScreenExample() {
//    val viewModel: LanguageViewModel = viewModel()
//
//    // Use the custom function for all strings that should dynamically update
//    val greetingText = localizedStringResource(id = R.string.greeting)
//    val appTitle = localizedStringResource(id = R.string.app_name)
//
//    Text(text = appTitle)
//    Text(text = greetingText)
//
//    Button(onClick = { viewModel.setAppLanguage("es") }) {
//        Text(text = "Cambiar a Español")
//    }
//
//    Button(onClick = { viewModel.setAppLanguage("en") }) {
//        Text(text = "Switch to English")
//    }
//}

