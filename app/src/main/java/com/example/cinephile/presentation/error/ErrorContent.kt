package com.example.cinephile.presentation.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A reusable Compose component that displays error messages and a retry button for various error states.
 */
@Composable
fun ErrorContent(
    error: MovieError,
    onRetry: () -> Unit
) {
    val message = when (error) {
        MovieError.NoInternet ->
            "No internet connection. Please check your connection and try again."

        MovieError.Server ->
            "The server is currently unavailable. Please try again later."

        MovieError.Unknown ->
            "Something went wrong. Please try again."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry
        ) {
            Text("Retry")
        }
    }
}
