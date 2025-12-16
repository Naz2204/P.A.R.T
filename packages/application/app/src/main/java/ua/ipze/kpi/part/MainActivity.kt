package ua.ipze.kpi.part

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import ua.ipze.kpi.part.database.ArtDatabase
import ua.ipze.kpi.part.database.migration_4_5
import ua.ipze.kpi.part.providers.MainActivityData
import ua.ipze.kpi.part.providers.MainActivityDataProvider
import ua.ipze.kpi.part.providers.static.GlobalApplicationContext
import ua.ipze.kpi.part.router.AppRouter
import ua.ipze.kpi.part.services.geogetter.LocationViewModel
import ua.ipze.kpi.part.ui.theme.PARTTheme
import ua.ipze.kpi.part.utils.Biometry
import ua.ipze.kpi.part.views.DatabaseViewModel
import ua.ipze.kpi.part.views.LanguageViewModel
import ua.ipze.kpi.part.views.PasswordViewModel

class MainActivity : AppCompatActivity() {
    private val artDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            ArtDatabase::class.java,
            "art.db"
        ).addMigrations(migration_4_5).fallbackToDestructiveMigration().build()
    }

    private val promptManager by lazy {
        Biometry(this)
    }

    private val locationViewModel: LocationViewModel by viewModels()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                locationViewModel.fetchLocation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        enableEdgeToEdge()

        GlobalApplicationContext.init(this)

        setContent {
            val languageViewModel: LanguageViewModel = viewModel()
            val passwordViewModel: PasswordViewModel = viewModel()
            val databaseViewModel: DatabaseViewModel = viewModel()
            val mainActivityData = remember { MainActivityData(this) }

            databaseViewModel.initialize(artDatabase)
            checkAndRequestLocation()
            CompositionLocalProvider(MainActivityDataProvider provides mainActivityData) {
                PARTTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AppRouter(
                            innerPadding,
                            languageViewModel,
                            passwordViewModel,
                            databaseViewModel,
                            promptManager,
                            locationViewModel
                        )
                    }
                }
            }
        }
    }

    private fun checkAndRequestLocation() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                locationViewModel.fetchLocation()
            }

            else -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

}
