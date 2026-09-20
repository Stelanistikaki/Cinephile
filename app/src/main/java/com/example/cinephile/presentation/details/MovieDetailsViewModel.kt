package com.example.cinephile.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinephile.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel for the movie details screen, responsible for fetching detailed movie data from the repository.
 */
@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailUiState())
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = MovieDetailUiState(isLoading = true)

            try {
                val movie = repository.getMovieDetails(movieId)

                _uiState.value = MovieDetailUiState(
                    movie = movie
                )
            } catch (e: Exception) {
                _uiState.value = MovieDetailUiState(
                    error = e.message ?: "Something went wrong"
                )
            }
        }
    }
}