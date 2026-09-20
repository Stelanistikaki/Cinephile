package com.example.cinephile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cinephile.presentation.details.MovieDetailScreen
import com.example.cinephile.presentation.popular.PopularListScreen

/**
 * Defines the navigation host and routes for the application, connecting the popular list and movie details screens.
 */
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Movies.route
    ) {
        composable(route = Screen.Movies.route) {
            PopularListScreen (
                onMovieClick = { movieId ->
                    navController.navigate( Screen.MovieDetail.createRoute(movieId))
                }
            )
        }

        composable(
            route = Screen.MovieDetail.route,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val movieId = backStackEntry.arguments?.getInt("movieId")

            if (movieId != null) {
                MovieDetailScreen(movieId = movieId, onBackClick = { navController.popBackStack() })
            }
        }
    }
}