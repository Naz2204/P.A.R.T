package ua.ipze.kpi.part.views

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ua.ipze.kpi.part.utils.PrefManager
import java.util.Locale

data class AppLocaleState(
    val currentLocale: Locale = Locale.getDefault()
)

class LanguageViewModel(application: Application) : AndroidViewModel(application) {

    private val prefManager = PrefManager(application.applicationContext)

    var localeState by mutableStateOf(
        AppLocaleState(Locale.forLanguageTag(prefManager.DEFAULT_LANGUAGE))
    )
        private set

    init {
        getPersistedLocale()
    }

    private fun getPersistedLocale() {
        viewModelScope.launch {
            val languageCode =
                prefManager.getLanguagePreference().first()

            localeState = localeState.copy(
                currentLocale = Locale.forLanguageTag(languageCode)
            )
        }
    }

    fun setAppLanguage(languageCode: String) {
        val newLocale = Locale.forLanguageTag(languageCode)

        viewModelScope.launch {
            prefManager.setLanguagePreference(languageCode)
        }

        localeState = localeState.copy(currentLocale = newLocale)
    }
}

fun getLocalizedContext(context: Context, locale: Locale): Context {
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    return context.createConfigurationContext(config)
}

@Composable
fun localizedStringResource(
    @StringRes id: Int,
    viewModel: LanguageViewModel,
    vararg formatArgs: Any
): String {
    val context = LocalContext.current
    val currentLocale = viewModel.localeState.currentLocale

    val localizedContext = remember(currentLocale) {
        getLocalizedContext(context, currentLocale)
    }

    return localizedContext.resources.getString(id, *formatArgs)
}


