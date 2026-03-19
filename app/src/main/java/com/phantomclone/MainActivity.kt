package com.phantomclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.phantomclone.ui.PhantomCloneNavHost
import com.phantomclone.ui.theme.PhantomCloneTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity — single-activity architecture.
 * Hosts the Compose navigation graph via [PhantomCloneNavHost].
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhantomCloneTheme {
                PhantomCloneNavHost()
            }
        }
    }
}
