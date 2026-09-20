package com.example.cinephile.data.local.entity

import com.example.cinephile.domain.model.Movie

/**
 * Extension functions to map between the local database entity and the domain movie model.
 */

fun Movie.toFavouriteEntity(): FavouriteMovieEntity {
    return FavouriteMovieEntity(
        id = id,
        title = title,
        posterPath = posterPath,
        releaseDate = releaseDate,
        rating = rating
    )
}

fun FavouriteMovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = posterPath,
        releaseDate = releaseDate,
        rating = rating
    )
}
