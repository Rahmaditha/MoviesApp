package com.cookiss.moviesapp.domain.repository

import androidx.paging.PagingData
import com.cookiss.movieapp.domain.model.genre_list.GenreMovieResponse
import com.cookiss.movieapp.domain.model.popular_list.PopularMovies
import com.cookiss.moviesapp.domain.model.popular_list.PopularMoviesResponse
import com.cookiss.moviesapp.domain.model.movie_detail.MovieDetailResponse
import com.cookiss.moviesapp.domain.model.movie_videos.MovieVideoResponse
import com.cookiss.moviesapp.domain.model.reviews.Reviews
import com.cookiss.moviesapp.domain.model.reviews.ReviewsResponse
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

    suspend fun getReviews(
        page: Int,
        movieId: String
    ) : Flow<ReviewsResponse>

    fun fetchReviews(movieId: String): Flow<PagingData<Reviews>>
    fun fetchMovies(with_genres: String): Flow<PagingData<PopularMovies>>
}