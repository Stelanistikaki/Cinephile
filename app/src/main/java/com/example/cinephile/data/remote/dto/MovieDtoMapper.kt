package com.example.cinephile.data.remote.dto

import com.example.cinephile.domain.model.Movie

/**
 * Extension functions to map the remote movie data transfer object into the basic domain movie model.
 */
fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        releaseDate = releaseDate,
        rating = voteAverage
    )
}