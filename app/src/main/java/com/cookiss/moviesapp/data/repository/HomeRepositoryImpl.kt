package com.cookiss.moviesapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cookiss.movieapp.domain.model.genre_list.GenreMovieResponse
import com.cookiss.movieapp.domain.model.popular_list.PopularMovies
import com.cookiss.moviesapp.domain.model.popular_list.PopularMoviesResponse
import com.cookiss.moviesapp.data.remote.ApiService
import com.cookiss.moviesapp.data.remote.MoviePagingSource
import com.cookiss.moviesapp.data.remote.ReviewPagingSource
import com.cookiss.moviesapp.domain.model.movie_detail.MovieDetailResponse
import com.cookiss.moviesapp.domain.model.movie_videos.MovieVideoResponse
import com.cookiss.moviesapp.domain.model.reviews.Reviews
import com.cookiss.moviesapp.domain.model.reviews.ReviewsResponse
import com.cookiss.moviesapp.domain.repository.HomeRepository
import com.cookiss.moviesapp.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val apiService : ApiService
) : HomeRepository{
    override suspend fun getGenreList(): Flow<GenreMovieResponse> {
        return flow {
            val genreList = apiService.getGenreList(Constants.API_KEY)
            emit(genreList)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getPopularMovies(): Flow<PopularMoviesResponse> {
        return flow {
            val popularMovies = apiService.getPopularMovies(Constants.API_KEY)
            emit(popularMovies)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMoviesByGenre(page: Int, genreId: String): Flow<PopularMoviesResponse> {
        return flow {
            val moviesByGenre = apiService.getMoviesByGenre(page, genreId, Constants.API_KEY)
            emit(moviesByGenre)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMovieDetails(movieId: String): Flow<MovieDetailResponse> {
        return flow {
            val movieDetail = apiService.getMoviesDetail(movieId, Constants.API_KEY)
            emit(movieDetail)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMovieVideos(movieId: String): Flow<MovieVideoResponse> {
        return flow {
            val movieVideos = apiService.getMoviesVideos(movieId, Constants.API_KEY)
            emit(movieVideos)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getReviews(page: Int, movieId: String): Flow<ReviewsResponse> {
        return flow {
            val reviewResult = apiService.getReviews(movieId, page,Constants.API_KEY)
            emit(reviewResult)
        }.flowOn(Dispatchers.IO)
    }

    override fun fetchReviews(movieId: String): Flow<PagingData<Reviews>> {
        return Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = false)
        ) {
            ReviewPagingSource(apiService, movieId)
        }.flow
    }

    override fun fetchMovies(with_genres: String): Flow<PagingData<PopularMovies>> {
        return Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = false)
        ) {
            MoviePagingSource(apiService, with_genres)
        }.flow
    }

}