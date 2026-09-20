package com.example.cinephile.domain.usecase

import com.example.cinephile.domain.model.Movie
import com.example.cinephile.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetFavouritesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetFavouritesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetFavouritesUseCase(repository)
    }

    @Test
    fun `getFavorites should return favorites from repository`() = runTest {

        val movies = listOf(
            Movie(
                id = 1,
                title = "Batman",
                posterPath = null,
                releaseDate = "2025-01-01",
                rating = 8.0
            ),
            Movie(
                id = 2,
                title = "Superman",
                posterPath = null,
                releaseDate = "2025-02-01",
                rating = 7.5
            )
        )

        every {
            repository.getFavourites()
        } returns flowOf(movies)

        val actual = useCase
            .getFavourites().first()

        assertEquals(movies, actual)

        coVerify(exactly = 0) {
            repository.addFavourite(any())
        }
    }

    @Test
    fun `toggleFavourite should remove movie when it is already favorite`() = runTest {

        val movie = Movie(
            id = 1,
            title = "Batman",
            posterPath = null,
            releaseDate = "2025-01-01",
            rating = 8.0
        )

        coEvery {
            repository.removeFavourite(movie)
        } returns Unit

        useCase.toggleFavourite(
            movie = movie,
            isFavorite = true
        )

        coVerify(exactly = 1) {
            repository.removeFavourite(movie)
        }

        coVerify(exactly = 0) {
            repository.addFavourite(any())
        }
    }

    @Test
    fun `toggleFavourite should add movie when it is not favorite`() = runTest {

        val movie = Movie(
            id = 1,
            title = "Batman",
            posterPath = null,
            releaseDate = "2025-01-01",
            rating = 8.0
        )

        coEvery {
            repository.addFavourite(movie)
        } returns Unit

        useCase.toggleFavourite(
            movie = movie,
            isFavorite = false
        )

        coVerify(exactly = 1) {
            repository.addFavourite(movie)
        }

        coVerify(exactly = 0) {
            repository.removeFavourite(any())
        }
    }
}
