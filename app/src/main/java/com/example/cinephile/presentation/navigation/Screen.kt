package com.example.cinephile.presentation.navigation

/**
 * A sealed class that defines the available navigation routes and provides helper methods for creating routes with arguments.
 */
sealed class Screen(val route: String) {

    data object Movies : Screen("movies")

    data object MovieDetail : Screen("movie/{movieId}") {
        fun createRoute(movieId: Int): String {
            return "movie/$movieId"
        }
    }
}