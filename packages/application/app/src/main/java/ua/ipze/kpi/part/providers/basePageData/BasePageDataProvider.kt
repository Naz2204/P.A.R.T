package ua.ipze.kpi.part.providers.basePageData

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController

class BasePageData(val innerPadding: PaddingValues, val nav: NavController) {}
val BasePageDataProvider = compositionLocalOf<BasePageData>{ error("${BasePageData::class.simpleName} provider not provided") }