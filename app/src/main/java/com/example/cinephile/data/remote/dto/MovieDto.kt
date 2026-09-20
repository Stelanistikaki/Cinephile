package com.example.cinephile.data.remote.dto

import com.example.cinephile.domain.model.Movie
import com.google.gson.annotations.SerializedName

/**
 * A data transfer object for basic movie information received from the TMDB API, along with a domain mapper.
 */

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("release_date")
    val releaseDate: String?,

    @SerializedName("vote_average")
    val voteAverage: Double
)