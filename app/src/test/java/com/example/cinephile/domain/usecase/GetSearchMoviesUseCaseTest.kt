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

class GetSearchMoviesUseCaseTest {
    private lateinit var repository: MovieRepository
    private lateinit var useCase: GetSearchMoviesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetSearchMoviesUseCase(repository)
    }

    @Test
    fun `invoke should return search results from repository`() = runTest {
        val movies = listOf(
            Movie(
                id = 1,
                title = "batman",
                posterPath = null,
                releaseDate = "2025-01-01",
                rating = 8.0
            )
        )
        val result = PaginatedResult(
            items = movies,
            page = 1,
            totalPages = 5
        )

        coEvery { repository.searchMovies(query = "batman", page = 1) } returns result
        val actual = useCase(
            query = "batman",
            page = 1
        )

        assertEquals(result, actual)
        coVerify(exactly = 1) {
            repository.searchMovies(
                query = "batman",
                page = 1
            )
        }
    }

    @Test
    fun `invoke should return empty list when no movies are found`() = runTest {

        val result = PaginatedResult<Movie>(
            items = emptyList(),
            page = 1,
            totalPages = 1
        )

        coEvery {
            repository.searchMovies(
                query = "thismoviedoesnotexist",
                page = 1
            )
        } returns result

        val actual = useCase(
            query = "thismoviedoesnotexist",
            page = 1
        )

        assertEquals(emptyList<Movie>(), actual.items)
        assertEquals(1, actual.page)
        assertEquals(1, actual.totalPages)

        coVerify(exactly = 1) {
            repository.searchMovies(
                query = "thismoviedoesnotexist",
                page = 1
            )
        }
    }
}