package com.cookiss.moviesapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookiss.movieapp.domain.model.genre_list.GenreMovieResponse
import com.cookiss.movieapp.domain.model.popular_list.PopularMoviesResponse
import com.cookiss.moviesapp.domain.model.movie_detail.MovieDetailResponse
import com.cookiss.moviesapp.domain.model.movie_videos.MovieVideoResponse
import com.cookiss.moviesapp.domain.repository.HomeRepository
import com.cookiss.moviesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel(){

    val TAG = this.javaClass.simpleName

    private val _genreListResult = MutableLiveData<Resource<GenreMovieResponse>>()
    val genreListResult : LiveData<Resource<GenreMovieResponse>> = _genreListResult

    private val _popularMoviesResult = MutableLiveData<Resource<PopularMoviesResponse>>()
    val popularMoviesResult : LiveData<Resource<PopularMoviesResponse>> = _popularMoviesResult

    private val _movieDetailResult = MutableLiveData<Resource<MovieDetailResponse>>()
    val movieDetailResult : LiveData<Resource<MovieDetailResponse>> = _movieDetailResult

    private val _movieVideosResult = MutableLiveData<Resource<MovieVideoResponse>>()
    val movieVideosResult : LiveData<Resource<MovieVideoResponse>> = _movieVideosResult

    fun getGenreList(){
        viewModelScope.launch {
            repository.getGenreList()
                .onStart {
                    _genreListResult.postValue(Resource.Loading(true))
                }
                .catch {
                    it.message?.let { message ->
                        _genreListResult.postValue(Resource.Error(null, message))
                    }
                }
                .collect { genreList ->
                    _genreListResult.postValue(Resource.Success(genreList))

                }
        }
    }

    fun getPopularMovies(){
        viewModelScope.launch {
            repository.getPopularMovies()
                .onStart {
                    _popularMoviesResult.postValue(Resource.Loading(true))
                }
                .catch {
                    it.message?.let { message ->
                        _popularMoviesResult.postValue(Resource.Error(null, message))
                    }
                }
                .collect { popularMovies ->
                    _popularMoviesResult.postValue(Resource.Success(popularMovies))

                }
        }
    }

    fun getMoviesByGenre(genreId: String){
        viewModelScope.launch {
            repository.getMoviesByGenre(1, genreId)
                .onStart {
                    _popularMoviesResult.postValue(Resource.Loading(true))
                }
                .catch {
                    it.message?.let { message ->
                        _popularMoviesResult.postValue(Resource.Error(null, message))
                    }
                }
                .collect { popularMovies ->
                    _popularMoviesResult.postValue(Resource.Success(popularMovies))

                }
        }
    }

    fun getMovieDetail(movieId: String){
        viewModelScope.launch {
            repository.getMovieDetails(movieId)
                .onStart {
                    _movieDetailResult.postValue(Resource.Loading(true))
                }
                .catch {
                    it.message?.let { message ->
                        _movieDetailResult.postValue(Resource.Error(null, message))
                    }
                }
                .collect { movieDetail ->
                    _movieDetailResult.postValue(Resource.Success(movieDetail))

                }
        }
    }

    fun getMovieVideos(movieId: String){
        viewModelScope.launch {
            repository.getMovieVideos(movieId)
                .onStart {
                    _movieVideosResult.postValue(Resource.Loading(true))
                }
                .catch {
                    it.message?.let { message ->
                        _movieVideosResult.postValue(Resource.Error(null, message))
                    }
                }
                .collect { movieVideos ->
                    _movieVideosResult.postValue(Resource.Success(movieVideos))

                }
        }
    }
}