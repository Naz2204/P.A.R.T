package ua.ipze.kpi.part.router

import android.content.Context
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ua.ipze.kpi.part.pages.info.InfoPage
import ua.ipze.kpi.part.pages.start.StartPage
import ua.ipze.kpi.part.providers.basePageData.BasePageData
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import ua.ipze.kpi.part.providers.languageChange.LanguageViewModel

@Composable
fun AppRouter(innerPadding: PaddingValues, languageViewModel: LanguageViewModel) {
    val navController = rememberNavController()
    val basicPageData = remember { BasePageData(innerPadding, navController) }

    CompositionLocalProvider(BasePageDataProvider provides basicPageData) {
        NavHost(
            navController = navController, startDestination = StartPageData,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }) {
//            composable<StartPageData> { StartPage(languageViewModel = languageViewModel) }
            composable<StartPageData> { StartPage() }
//            composable<InfoPageData> { InfoPage() }
        }
    }
}