package com.example.moviestime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.moviestime.data.model.Movie
import com.example.moviestime.data.model.CastMember
import com.example.moviestime.data.model.Director
import com.example.moviestime.data.remote.toMovie
import com.example.moviestime.data.repository.MovieDetailsRepository

class MovieDetailsViewModel : ViewModel() {
    private val repository = MovieDetailsRepository()
    private val _movieDetails = MutableStateFlow<Movie?>(null)
    val movieDetails: StateFlow<Movie?> = _movieDetails.asStateFlow()
    private val _similarMovies = MutableStateFlow<List<Movie>>(emptyList())
    val similarMovies: StateFlow<List<Movie>> = _similarMovies.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val details = repository.getMovieDetails(movieId)

                val videosResponse = repository.getMovieVideos(movieId)
                val similarMoviesResponse = repository.getSimilarMovies(movieId)
                val creditsResponse = repository.getMovieCredits(movieId)
                
                // Debug: Log all videos
                android.util.Log.d("MovieDetailsViewModel", "Total videos found: ${videosResponse.results.size}")
                videosResponse.results.forEachIndexed { index, video ->
                    android.util.Log.d("MovieDetailsViewModel", "Video $index: site=${video.site}, type=${video.type}, key=${video.key}, name=${video.name}")
                }
                
                val trailerKey = videosResponse.results.firstOrNull {
                    it.site == "YouTube" && it.type == "Trailer"
                }?.key
                
                // Debug: Log trailer key
                if (trailerKey != null) {
                    android.util.Log.d("MovieDetailsViewModel", "Found trailer key: $trailerKey")
                } else {
                    android.util.Log.w("MovieDetailsViewModel", "No trailer found! Available videos:")
                    videosResponse.results.forEach { video ->
                        android.util.Log.w("MovieDetailsViewModel", "  - ${video.site}/${video.type}: ${video.key}")
                    }
                }

                // Extract director from crew
                val directorCrew = creditsResponse.crew.firstOrNull { 
                    it.job == "Director" 
                }
                val director = directorCrew?.name ?: ""
                val directorInfo = directorCrew?.let {
                    Director(
                        id = it.id,
                        name = it.name,
                        profilePath = it.profile_path?.let { path -> 
                            "https://image.tmdb.org/t/p/w185$path" 
                        }
                    )
                }

                // Extract top cast members (first 5) with images
                val cast = creditsResponse.cast
                    .sortedBy { it.order }
                    .take(5)
                    .joinToString(", ") { it.name }
                
                val castMembers = creditsResponse.cast
                    .sortedBy { it.order }
                    .take(5)
                    .map {
                        CastMember(
                            id = it.id,
                            name = it.name,
                            character = it.character,
                            profilePath = it.profile_path?.let { path -> 
                                "https://image.tmdb.org/t/p/w185$path" 
                            }
                        )
                    }

                _movieDetails.value = details.toMovie(
                    trailerKey = trailerKey,
                    director = director,
                    cast = cast,
                    directorInfo = directorInfo,
                    castMembers = castMembers
                )
                _similarMovies.value = similarMoviesResponse.results.map { it.toMovie() }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}