package com.cookiss.moviesapp.domain.repository

import com.cookiss.movieapp.domain.model.genre_list.GenreMovieResponse
import com.cookiss.movieapp.domain.model.popular_list.PopularMoviesResponse
import com.cookiss.moviesapp.domain.model.movie_detail.MovieDetailResponse
import com.cookiss.moviesapp.domain.model.movie_videos.MovieVideoResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getGenreList(
    ) : Flow<GenreMovieResponse>

    suspend fun getPopularMovies(
    ) : Flow<PopularMoviesResponse>

    suspend fun getMoviesByGenre(
        page: Int,
        genreId: String
    ) : Flow<PopularMoviesResponse>

    suspend fun getMovieDetails(
        movieId: String
    ) : Flow<MovieDetailResponse>

    suspend fun getMovieVideos(
        movieId: String
    ) : Flow<MovieVideoResponse>
}