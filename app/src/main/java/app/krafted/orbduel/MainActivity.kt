package app.krafted.orbduel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import dagger.hilt.android.AndroidEntryPoint
import app.krafted.orbduel.ui.navigation.OrbDuelNavHost
import app.krafted.orbduel.ui.theme.OrbDuelTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrbDuelTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    OrbDuelNavHost()
                }
            }
        }
    }
}
