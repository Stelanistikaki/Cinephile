package com.example.cinephile.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * A data transfer object representing the detailed movie information returned by the TMDB API, including a domain mapper.
 */

data class MovieDetailsDto(
    val id: Int,
    val title: String,
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("vote_average")
    val voteAverage: Double,

    val runtime: Int?,

    val genres: List<GenreDto>
)

data class GenreDto(
    val id: Int,
    val name: String
)