package com.cookiss.moviesapp.domain.model.popular_list

import com.cookiss.movieapp.domain.model.popular_list.PopularMovies

data class PopularMoviesResponse(
    val page: Int,
    val results: List<PopularMovies>,
    val total_pages: Int,
    val total_results: Int
)