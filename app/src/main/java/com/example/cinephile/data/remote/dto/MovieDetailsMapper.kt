package com.example.cinephile.data.remote.dto

import com.example.cinephile.domain.model.MovieDetails

/**
 * Extension functions to map the remote movie details data transfer object into the domain model.
 */
fun MovieDetailsDto.toDomain(): MovieDetails {
    return MovieDetails(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        rating = voteAverage,
        runtime = runtime,
        genres = genres.map { it.name }
    )
}