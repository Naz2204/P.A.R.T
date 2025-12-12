package ua.ipze.kpi.part.router

import android.content.Context
import android.util.Log
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
import ua.ipze.kpi.part.pages.creation.CreationPage
import ua.ipze.kpi.part.pages.editor.EditorPage
import ua.ipze.kpi.part.pages.gallery.GalleryPage
import ua.ipze.kpi.part.providers.basePageData.BasePageData
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import ua.ipze.kpi.part.providers.languageChange.LanguageViewModel

@Composable
fun AppRouter(innerPadding: PaddingValues, languageViewModel: LanguageViewModel) {
    val navController = rememberNavController()
    val basicPageData = remember { BasePageData(innerPadding, navController) }
    Log.d("Recreate", "Router recreated")
    CompositionLocalProvider(BasePageDataProvider provides basicPageData) {
        NavHost(
            navController = navController, startDestination = GalleryAppData,
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
            composable<CreatePasswordPageData> {  }
            composable<LoginPageData> {  }
            composable<CreateArtPageData> { CreationPage(languageViewModel) }
            composable<GalleryAppData> { GalleryPage() }
            composable<EditorAppData> { EditorPage() }
        }
    }
}