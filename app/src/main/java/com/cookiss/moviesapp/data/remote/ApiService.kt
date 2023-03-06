package com.cookiss.moviesapp.data.remote

import com.cookiss.movieapp.domain.model.genre_list.GenreMovieResponse
import com.cookiss.movieapp.domain.model.popular_list.PopularMoviesResponse
import com.cookiss.moviesapp.domain.model.movie_detail.MovieDetailResponse
import com.cookiss.moviesapp.domain.model.movie_videos.MovieVideoResponse
import com.cookiss.moviesapp.domain.model.reviews.ReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("genre/movie/list")
    suspend fun getGenreList(
        @Query("api_key") api_key: String
    ): GenreMovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") api_key: String
    ): PopularMoviesResponse


    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("page") page: Int,
        @Query("with_genres") with_genres: String,
        @Query("api_key") api_key: String,
    ): PopularMoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMoviesDetail(
        @Path("movie_id") movieId: String,
        @Query("api_key") api_key: String,
    ): MovieDetailResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMoviesVideos(
        @Path("movie_id") movieId: String,
        @Query("api_key") api_key: String,
    ): MovieVideoResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getReviews(
        @Path("movie_id") movieId: String,
        @Query("page") page: Int,
        @Query("api_key") api_key: String,
    ): ReviewsResponse
}