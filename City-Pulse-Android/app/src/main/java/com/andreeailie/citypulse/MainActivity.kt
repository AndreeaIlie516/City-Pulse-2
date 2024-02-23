package com.andreeailie.citypulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
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
                Surface(
                    modifier = Modifier.fillMaxHeight(),
                    color = colorResource(R.color.not_really_white)
                ) {
                    PopularEventsScreen()
                }
            }
        }
    }
}