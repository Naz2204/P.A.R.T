package ua.ipze.kpi.part.pages.info

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.LocaleListCompat
import ua.ipze.kpi.part.router.StartPageData
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider
import java.util.Locale


@Composable
fun InfoPage(setLanguage: (Context, String) -> Unit) {
    val basic = BasePageDataProvider.current;

    Column(Modifier.padding(basic.innerPadding)) {
        Text(
            text = "This is the biggest piece of dog shit i've ever seen"
        )

        Button(onClick = {}) {
            Text(text = stringResource(R.string.world))
        }

        Button(onClick = { basic.nav.navigate(StartPageData) }) {
            Text("Go to start")
        }
    }
}
