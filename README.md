# iTunes Master Detail

## Overview
This Android application is a master-detail app that allows users to search for movies from the iTunes Search API, view detailed information about each movie, and manage their favorites.

## Features

### 1. Movie Favorites
- Users can **favorite** movies directly from the list screen.
- Users can also favorite movies from the detail screen.
- Favorited movies are displayed as favorites on the list screen.
- The app supports offline access to the list of favorite movies.

### 2. Movie Search
- Users can search for movies in real-time as they type.
- An empty screen is shown when the search input is empty.

## API Reference
Data is fetched from the iTunes Search API using the following endpoint:
https://itunes.apple.com/search?term=star&country=au&media=movie
For more details, refer to the [iTunes Web Service Documentation](https://affiliate.itunes.apple.com/resources/documentation/itunes-store-web-service-search-api/#searching).

## User Interface
- The main screen displays a list of movies with:
    - **Track Name**
    - **Artwork** (a placeholder image if the URL fails to load)
    - **Price**
    - **Genre**
- The detail screen provides a **Long Description** for each movie.

## Persistence
- The app uses **Room** for local data persistence to save:
    - The last visited screen.
    - A date showing the last time the user visited, displayed in the list header.

## Additional Features
  - The app can play video
  - The app can see the favorite list
  - The app can navigate webview when view website clicked

## Architecture
The app follows the **MVVM** (Model-View-ViewModel) design pattern, utilizing:
- **ViewModel** for managing UI-related data.
- **Repository Pattern** for data access.

## Tech Stack
- **Kotlin**
- **Jetpack Compose** for UI
- **Coroutines** for asynchronous programming
- **Flows** and **ViewModel**
- **Room** for database persistence
- **DataStore** for data store persistence
- **Retrofit** and **OkHttp** for network requests
- **Dagger Hilt** for dependency injection
- **Unit Testing** to ensure functionality
- **Exoplayer** for Video Player

## Installation
To run this application locally:

1. Clone the repository:
   ```bash
   git clone https://github.com/ariesmercado/Android-Developer-Coding-Challenge.git


