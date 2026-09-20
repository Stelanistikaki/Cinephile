# Cinephile

A simple Android application for discovering movies using the [TMDB API](https://developer.themoviedb.org/docs).

This project was built as a take-home assignment with a focus on **clean architecture, separation of concerns, testability, and a straightforward user experience**.

## Features

* Browse popular movies from TMDB
* Search movies by title
* Debounced search to avoid unnecessary API requests
* Search falls back to popular movies when the query is cleared or shorter than 3 characters
* Paginated movie loading
* Movie details screen
* Display movie title, release year, rating, and poster
* Display movie overview, runtime, genres, and rating on the details screen
* Add and remove movies from favourites
* Persist favourites locally using Room
* Favourite state is shared across the movie list
* Loading states
* Error handling for:

  * No internet connection
  * Server/API errors
  * Unknown errors
* Retry functionality after an error
* Navigation between movie list and movie details
* Defensive duplicate handling when loading additional pages

## Tech Stack

* **Kotlin**
* **Jetpack Compose** — UI
* **MVVM** — architecture
* **Hilt** — dependency injection
* **Retrofit** — networking
* **OkHttp** — HTTP client configuration and authentication
* **Gson** — JSON parsing
* **Room** — local persistence
* **Kotlin Coroutines & Flow** — asynchronous operations and reactive state
* **Navigation Compose** — screen navigation
* **Coil** — image loading
* **JUnit + MockK + kotlinx-coroutines-test** — unit testing

## Architecture

The project follows a layered architecture with clear separation between the presentation, domain, and data layers.

```text
presentation
     ↓
  domain
     ↓
   data
```

### Presentation

Contains Compose screens, ViewModels, and UI state.

Responsible for:

* Rendering the UI
* Handling user interactions
* Managing screen state
* Triggering domain operations

### Domain

Contains business logic and abstractions that are independent of Android framework details.

Includes:

* Domain models
* Repository interfaces
* Use cases

### Data

Contains implementations of data access.

Includes:

* TMDB API service
* DTOs and mappers
* Repository implementation
* Room database
* DAOs
* Dependency injection modules

This separation keeps the business logic independent from concrete data sources and makes the code easier to test and maintain.

## API

The application uses the [TMDB API](https://developer.themoviedb.org/docs).

The following endpoints are currently used:

```text
GET /movie/popular
GET /search/movie
GET /movie/{movieId}
```

Authentication is handled through an OkHttp interceptor using the TMDB Bearer token.

### API Key Setup

The API token is intentionally **not committed to the repository**.

Create an `api.properties` file in the **project root**:

```text
Cinephile/
├── api.properties
├── build.gradle.kts
├── settings.gradle.kts
└── app/
```

Add the following:

```properties
TMDB_API_TOKEN=your_token_here
```

The file is included in `.gitignore` so the token is not committed to source control.

For a reviewer, the token can be provided separately together with the project.

An `api.properties.example` file can also be provided as a reference:

```properties
TMDB_API_TOKEN=
```

> **Note:** An API token embedded in a distributed Android application cannot be considered a true secret. The purpose of this setup is to avoid accidentally committing the token to source control.

## Pagination

The movie list supports pagination using TMDB's `page` parameter.

The UI keeps track of:

* Current page
* Total available pages
* Loading state for the next page

When additional results are loaded, duplicate movie IDs are filtered before updating the list.

## Favourites

Favourite movies are persisted locally using Room.

The local database stores the information required by the movie list:

```text
id
title
posterPath
releaseDate
rating
```

The favourite state is exposed through Kotlin `Flow`, allowing the UI to reactively update when a movie is added or removed.

## Error Handling

Network and API failures are mapped into a small set of UI-friendly error states:

```text
NoInternet
Server
Unknown
```

The UI provides a retry action so the user can attempt the request again without restarting the application.

## Testing

Unit tests are included for the main domain and presentation logic.

The tests cover:

* Popular movies use case
* Search movies use case
* Favourites use case
* Movie list ViewModel
* Movie details ViewModel
* Successful API/repository responses
* Error handling
* Pagination
* Duplicate movie prevention
* Favourite state updates
* Search with no results

Testing uses:

* JUnit
* MockK
* kotlinx-coroutines-test

## Project Structure

```text
com.example.cinephile/
│
├── data/
│   ├── di/
│   ├── local/
│   │   ├── dao/
│   │   └── entity/
│   ├── remote/
│   │   └── dto/
│   └── repository/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
│
└── presentation/
    ├── details/
    ├── navigation/
    ├── pagination/
    └── popular/
```

## Possible Future Improvements

Some features that could be added with more development time:

* Offline-first movie caching
* More advanced pagination UX
* Dedicated movie details use case
* More UI/instrumentation tests
* Improved error differentiation based on HTTP status codes
* Favourite movies screen
* More extensive UI polish and animations

## Requirements

* Android Studio
* JDK compatible with the project's Gradle/Android Gradle Plugin configuration
* A TMDB API token

## Running the Project

1. Clone the repository.
2. Create `api.properties` in the project root.
3. Add your TMDB API token:

```properties
TMDB_API_TOKEN=your_token_here
```

4. Sync the Gradle project.
5. Build and run the application on an Android device or emulator.
