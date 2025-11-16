package ua.ipze.kpi.part.pages.start

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ua.ipze.kpi.part.router.InfoPageData
import ua.ipze.kpi.part.R
import ua.ipze.kpi.part.providers.basePageData.BasePageDataProvider

@Composable
fun StartPage() {
    val basic = BasePageDataProvider.current
    var counter by remember { mutableStateOf(0) }

    LazyColumn(modifier = Modifier.padding(basic.innerPadding)) {
        item {
            Text(
                text = stringResource(R.string.hello),
            )
        }
        items(10) { x: Int ->
            Button(onClick = { basic.nav.navigate(InfoPageData) }) {
                Text("Increment counter $x")
            }
        }
    }
}