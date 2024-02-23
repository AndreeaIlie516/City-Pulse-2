package com.andreeailie.citypulse

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.andreeailie.citypulse.bottomnavigation.BottomNavigation
import com.andreeailie.citypulse.bottomnavigation.NavigationGraph
import com.andreeailie.citypulse.events.Event
import com.andreeailie.citypulse.ui.theme.CityPulseTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            lifecycleScope.launch {
                viewModel.isLoading.collect { isLoading ->
                    if (!isLoading) {
                        splashScreenViewProvider.view.animate()
                            .alpha(0f)
                            .setDuration(200L)
                            .withEndAction {
                                splashScreenViewProvider.remove()
                            }
                    }
                }
            }
        }

        setContent {
            CityPulseTheme {
                MainScreenView()
            }
        }
    }
}
@Composable
fun SetTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(start = 25.dp, top = 10.dp)
    ) {
        Column(
            modifier = modifier
        )
        {
            Text(
                text = stringResource(R.string.app_name),
                textAlign = TextAlign.Left,
                color = colorResource(R.color.purple),
                style = MaterialTheme.typography.displaySmall,
                fontFamily = FontFamily(Font(R.font.sf_pro_display_bold))
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenView() {
    val navController = rememberNavController()
    val favoriteList = remember { mutableStateMapOf<Event, Boolean>() }

    Event.entries.forEach { event ->
        favoriteList[event] = false
    }

    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {
        SetTitle()
        NavigationGraph(navController = navController)
    }
}