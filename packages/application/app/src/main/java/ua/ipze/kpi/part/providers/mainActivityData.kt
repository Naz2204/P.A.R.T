package ua.ipze.kpi.part.providers

import android.app.Activity
import androidx.compose.runtime.compositionLocalOf


class MainActivityData(val activity: Activity)

val MainActivityDataProvider = compositionLocalOf<MainActivityData>{ error("${MainActivityData::class.simpleName} provider not provided") }