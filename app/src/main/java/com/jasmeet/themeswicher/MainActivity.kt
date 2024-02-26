package com.jasmeet.themeswicher

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jasmeet.themeswicher.ui.theme.ThemeSwicherTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var dataStoreUtil: DataStoreUtil

    override fun onCreate(savedInstanceState: Bundle?) {

        dataStoreUtil = DataStoreUtil(applicationContext)

        val systemTheme =
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    true
                }

                Configuration.UI_MODE_NIGHT_NO -> {
                    false
                }

                else -> {
                    false
                }
            }

        super.onCreate(savedInstanceState)
        setContent {

            val theme = dataStoreUtil.getTheme(systemTheme).collectAsState(initial = systemTheme)

            ThemeSwicherTheme(theme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting( dataStoreUtil = dataStoreUtil, theme = theme.value)
                }
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    dataStoreUtil: DataStoreUtil,
    theme: Boolean,
) {

    var themeSwitchState by rememberSaveable { mutableStateOf(theme) }

    val text = if (themeSwitchState) "Dark" else "Light"

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ThemeSwitch(
            themeSwitchState,
            onCheckedChange = {
                themeSwitchState = it
            },
            dataStoreUtil = dataStoreUtil
        )

        Text(
            text = "$text Mode",
            modifier = modifier
        )

    }


}


@Composable
fun ThemeSwitch(
    isCheck: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    dataStoreUtil: DataStoreUtil
) {
    val coroutineScope = rememberCoroutineScope()

    Switch(
        checked = isCheck,
        onCheckedChange = {
            onCheckedChange(it)
            coroutineScope.launch {
                dataStoreUtil.saveTheme(it)
            }
        },
        thumbContent = {
            if (isCheck) {
                Icon(
                    imageVector = Icons.Outlined.DarkMode,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.LightMode,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )

            }
        },
    )
}