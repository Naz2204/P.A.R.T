package ua.ipze.kpi.part.providers.basePageData

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import ua.ipze.kpi.part.views.LanguageViewModel

class BasePageData(val innerPadding: PaddingValues, val nav: NavController, val language: LanguageViewModel) {}
val BasePageDataProvider = compositionLocalOf<BasePageData>{ error("${BasePageData::class.simpleName} provider not provided") }