package com.example.cinephile.presentation.popular

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinephile.domain.model.Movie
import com.example.cinephile.domain.usecase.GetFavouritesUseCase
import com.example.cinephile.domain.usecase.GetPopularMoviesUseCase
import com.example.cinephile.domain.usecase.GetSearchMoviesUseCase
import com.example.cinephile.presentation.error.toPopularListError
import com.example.cinephile.presentation.pagination.PaginationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for the popular movies list, handling search queries, pagination logic, and favorite status updates.
 */
@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val popularMoviesUseCase: GetPopularMoviesUseCase,
    private val searchMoviesUseCase: GetSearchMoviesUseCase,
    private val favouritesUseCase: GetFavouritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PopularListUiState())
    val uiState: StateFlow<PopularListUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        observeSearch()
        observeFavourites()
    }

    private suspend fun loadPopularMovies() {
        _uiState.update {
            it.copy(isLoading = true, error = null)
        }

        try {
            val result = popularMoviesUseCase(
                page = 1
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    movies = result.items,
                    pagination = _uiState.value.pagination.copy(
                        currentPage = result.page,
                        totalPages = result.totalPages
                    )
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = e.toPopularListError()
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun observeSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->

                    resetPagination()

                    if (query.isNotEmpty()) {
                        searchMovies(query)
                    } else {
                        loadPopularMovies()
                    }
                }
        }
    }

    private fun observeFavourites() {
        viewModelScope.launch {
            favouritesUseCase.getFavourites().collect { favourites ->
                val favouriteIds = favourites
                    .map { it.id }
                    .toSet()
                _uiState.update {
                    it.copy(favouriteMovieIds = favouriteIds)
                }
            }
        }
    }

    private fun resetPagination() {
        _uiState.update { it.copy(pagination = PaginationState()) }
    }

    private suspend fun searchMovies(query: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        try {
            val result = searchMoviesUseCase(
                query = query,
                page = 1
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    movies = result.items,
                    error = null,
                    pagination = PaginationState(
                        currentPage = result.page,
                        totalPages = result.totalPages
                    )
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = e.toPopularListError()
                )
            }
        }
    }

    fun loadNextPage() {
        val pagination = _uiState.value.pagination

        if (pagination.isLoading || !pagination.hasNextPage || _uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    pagination = it.pagination.copy(
                        isLoading = true
                    )
                )
            }

            try {
                val nextPage = pagination.currentPage + 1

                val result = if (_searchQuery.value.length >= 3) {
                    searchMoviesUseCase(
                        query = _searchQuery.value,
                        page = nextPage
                    )
                } else {
                    popularMoviesUseCase(
                        page = nextPage
                    )
                }

                val existingIds = _uiState.value.movies
                    .map { it.id }
                    .toSet()
                val newMovies = result.items
                    .filter { movie -> movie.id !in existingIds }

                _uiState.update { currentState ->
                    currentState.copy(
                        movies = currentState.movies + newMovies,
                        pagination = currentState.pagination.copy(
                            currentPage = result.page,
                            totalPages = result.totalPages,
                            isLoading = false
                        )
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.toPopularListError(),
                        pagination = it.pagination.copy(
                            isLoading = false
                        )
                    )
                }
            }
        }
    }

    fun toggleFavourite(movie: Movie) {
        val isFavourite = movie.id in _uiState.value.favouriteMovieIds
        viewModelScope.launch {
            favouritesUseCase.toggleFavourite(
                movie = movie,
                isFavorite = isFavourite
            )
        }
    }

    fun retry() {
        viewModelScope.launch {
            if (_searchQuery.value.isNotEmpty()) {
                searchMovies(_searchQuery.value)
            } else {
                loadPopularMovies()
            }
        }
    }
}