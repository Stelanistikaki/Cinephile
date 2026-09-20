package com.example.cinephile.presentation.popular

import com.example.cinephile.MainDispatcherRule
import com.example.cinephile.domain.model.Movie
import com.example.cinephile.domain.model.PaginatedResult
import com.example.cinephile.domain.repository.MovieRepository
import com.example.cinephile.domain.usecase.GetFavouritesUseCase
import com.example.cinephile.domain.usecase.GetPopularMoviesUseCase
import com.example.cinephile.domain.usecase.GetSearchMoviesUseCase
import com.example.cinephile.presentation.error.MovieError
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class MovieListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: MovieRepository
    private lateinit var popularMoviesUseCase: GetPopularMoviesUseCase
    private lateinit var searchMoviesUseCase: GetSearchMoviesUseCase
    private lateinit var favouritesUseCase: GetFavouritesUseCase

    private lateinit var viewModel: MovieListViewModel

    @Before
    fun setup() {
        repository = mockk()

        popularMoviesUseCase = mockk()
        searchMoviesUseCase = mockk()
        favouritesUseCase = mockk()

        every {
            favouritesUseCase.getFavourites()
        } returns flowOf(emptyList())

        viewModel = MovieListViewModel(
            popularMoviesUseCase = popularMoviesUseCase,
            searchMoviesUseCase = searchMoviesUseCase,
            favouritesUseCase = favouritesUseCase
        )
    }

    @Test
    fun `initial load should display popular movies`() = runTest {

        val movies = listOf(
            movie(id = 1, title = "Batman"),
            movie(id = 2, title = "Superman")
        )

        val result = PaginatedResult(
            items = movies,
            page = 1,
            totalPages = 5
        )

        coEvery {
            popularMoviesUseCase(1)
        } returns result

        viewModel.retry()

        val state = viewModel.uiState.value

        assertEquals(movies, state.movies)
        assertEquals(1, state.pagination.currentPage)
        assertEquals(5, state.pagination.totalPages)
        assertEquals(null, state.error)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `initial load should show NoInternet when network fails`() = runTest {

        coEvery {
            popularMoviesUseCase(1)
        } throws IOException()

        viewModel.retry()

        val state = viewModel.uiState.value

        assertEquals(MovieError.NoInternet, state.error)
        assertEquals(false, state.isLoading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `search should display search results`() = runTest {

        val movies = listOf(
            movie(id = 10, title = "Batman")
        )

        val result = PaginatedResult(
            items = movies,
            page = 1,
            totalPages = 2
        )

        coEvery {
            searchMoviesUseCase(
                query = "batman",
                page = 1
            )
        } returns result

        viewModel.onSearchQueryChanged("batman")

        advanceTimeBy(500)
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertEquals(movies, state.movies)
        assertEquals(1, state.pagination.currentPage)
        assertEquals(2, state.pagination.totalPages)
        assertEquals(null, state.error)
    }

    @Test
    fun `search with no results should return empty movie list`() = runTest {

        val result = PaginatedResult<Movie>(
            items = emptyList(),
            page = 1,
            totalPages = 1
        )

        coEvery {
            searchMoviesUseCase(
                query = "unknownmovie",
                page = 1
            )
        } returns result

        viewModel.onSearchQueryChanged("unknownmovie")

        val state = viewModel.uiState.value

        assertTrue(state.movies.isEmpty())
        assertEquals(null, state.error)
    }

    @Test
    fun `loadNextPage should append new movies`() = runTest {

        val firstPage = PaginatedResult(
            items = listOf(
                movie(id = 1, title = "Movie 1"),
                movie(id = 2, title = "Movie 2")
            ),
            page = 1,
            totalPages = 2
        )

        val secondPage = PaginatedResult(
            items = listOf(
                movie(id = 3, title = "Movie 3"),
                movie(id = 4, title = "Movie 4")
            ),
            page = 2,
            totalPages = 2
        )

        coEvery {
            popularMoviesUseCase(1)
        } returns firstPage

        coEvery {
            popularMoviesUseCase(2)
        } returns secondPage

        viewModel.retry()

        viewModel.loadNextPage()

        val state = viewModel.uiState.value

        assertEquals(4, state.movies.size)
        assertEquals(
            listOf(1, 2, 3, 4),
            state.movies.map { it.id }
        )
        assertEquals(2, state.pagination.currentPage)
        assertEquals(false, state.pagination.isLoading)
    }

    @Test
    fun `loadNextPage should not add duplicate movies`() = runTest {

        val firstPage = PaginatedResult(
            items = listOf(
                movie(id = 1, title = "Movie 1"),
                movie(id = 2, title = "Movie 2")
            ),
            page = 1,
            totalPages = 2
        )

        val secondPage = PaginatedResult(
            items = listOf(
                movie(id = 2, title = "Movie 2"),
                movie(id = 3, title = "Movie 3")
            ),
            page = 2,
            totalPages = 2
        )

        coEvery {
            popularMoviesUseCase(1)
        } returns firstPage

        coEvery {
            popularMoviesUseCase(2)
        } returns secondPage

        viewModel.retry()

        viewModel.loadNextPage()

        val state = viewModel.uiState.value

        assertEquals(
            listOf(1, 2, 3),
            state.movies.map { it.id }
        )
    }

    @Test
    fun `favourites should update favourite movie ids`() = runTest {

        val favouriteMovies = listOf(
            movie(id = 10, title = "Batman"),
            movie(id = 20, title = "Superman")
        )

        every {
            favouritesUseCase.getFavourites()
        } returns flowOf(favouriteMovies)

        viewModel = MovieListViewModel(
            popularMoviesUseCase = popularMoviesUseCase,
            searchMoviesUseCase = searchMoviesUseCase,
            favouritesUseCase = favouritesUseCase
        )

        val state = viewModel.uiState.value

        assertEquals(
            setOf(10, 20),
            state.favouriteMovieIds
        )
    }

    private fun movie(
        id: Int,
        title: String
    ): Movie {
        return Movie(
            id = id,
            title = title,
            posterPath = null,
            releaseDate = "2025-01-01",
            rating = 8.0
        )
    }
}
