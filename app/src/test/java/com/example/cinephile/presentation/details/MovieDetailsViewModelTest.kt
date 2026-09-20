package com.example.cinephile.presentation.details

import com.example.cinephile.MainDispatcherRule
import com.example.cinephile.domain.model.MovieDetails
import com.example.cinephile.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MovieDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: MovieRepository = mockk()

    private val viewModel = MovieDetailViewModel(
        repository = repository
    )

    @Test
    fun `loadMovieDetails should display movie details when repository succeeds`() =
        runTest {
            val movieId = 123

            val movieDetails = MovieDetails(
                id = movieId,
                title = "Batman",
                overview = "A superhero movie",
                posterPath = "/poster.jpg",
                rating = 8.5,
                runtime = 120,
                genres = listOf("Action", "Drama")
            )

            coEvery {
                repository.getMovieDetails(movieId)
            } returns movieDetails

            viewModel.loadMovieDetails(movieId)

            val state = viewModel.uiState.value

            assertEquals(movieDetails, state.movie)
            assertEquals(false, state.isLoading)
            assertEquals(null, state.error)

            coVerify(exactly = 1) {
                repository.getMovieDetails(movieId)
            }
        }

    @Test
    fun `loadMovieDetails should show error when repository throws exception`() =
        runTest {
            val movieId = 123
            val errorMessage = "Something went wrong"

            coEvery {
                repository.getMovieDetails(movieId)
            } throws RuntimeException(errorMessage)

            viewModel.loadMovieDetails(movieId)

            val state = viewModel.uiState.value

            assertEquals(null, state.movie)
            assertEquals(false, state.isLoading)
            assertEquals(errorMessage, state.error)

            coVerify(exactly = 1) {
                repository.getMovieDetails(movieId)
            }
        }

    @Test
    fun `loadMovieDetails should use fallback error message when exception has no message`() =
        runTest {
            val movieId = 123

            coEvery {
                repository.getMovieDetails(movieId)
            } throws RuntimeException()

            viewModel.loadMovieDetails(movieId)

            val state = viewModel.uiState.value

            assertEquals(null, state.movie)
            assertEquals(false, state.isLoading)
            assertEquals(
                "Something went wrong",
                state.error
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadMovieDetails should set loading state while fetching movie`() =
        runTest {
            val movieId = 123

            coEvery {
                repository.getMovieDetails(movieId)
            } coAnswers {
                awaitCancellation()
            }

            viewModel.loadMovieDetails(movieId)

            runCurrent()

            val state = viewModel.uiState.value

            assertEquals(true, state.isLoading)
            assertEquals(null, state.movie)
            assertEquals(null, state.error)
        }
}
