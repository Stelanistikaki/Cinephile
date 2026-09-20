package com.example.cinephile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cinephile.presentation.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

/**
 * The single activity of the application that sets the Compose content and hosts the navigation graph.
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppNavGraph()
        }
    }
}