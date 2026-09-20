package com.example.cinephile.domain.usecase

import com.example.cinephile.domain.model.Movie
import com.example.cinephile.domain.model.PaginatedResult
import com.example.cinephile.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPopularMoviesUseCaseTest {

    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetPopularMoviesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetPopularMoviesUseCase(repository)
    }

    @Test
    fun `invoke should return popular movies from repository`() = runTest {

        val movies = listOf(
            Movie(
                id = 1,
                title = "Movie 1",
                posterPath = null,
                releaseDate = "2025-01-01",
                rating = 8.0
            )
        )

        val result = PaginatedResult(
            items = movies,
            page = 1,
            totalPages = 10
        )

        coEvery {
            repository.getPopularMovies(1)
        } returns result

        val actual = useCase(1)

        assertEquals(result, actual)

        coVerify(exactly = 1) {
            repository.getPopularMovies(1)
        }
    }
}
