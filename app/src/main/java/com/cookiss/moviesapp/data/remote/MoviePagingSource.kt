package com.cookiss.moviesapp.data.remote

import androidx.paging.PagingSource
import com.cookiss.movieapp.domain.model.popular_list.PopularMovies
import com.cookiss.moviesapp.domain.model.reviews.Reviews
import com.cookiss.moviesapp.util.Constants
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviePagingSource @Inject constructor(
    private val apiService: ApiService,
    private val with_genres: String
) : PagingSource<Int, PopularMovies>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PopularMovies> {

        val position = params.key ?: Constants.STARTING_PAGE_INDEX


        return try {
            val response = apiService.getMoviesByGenre(position, with_genres, Constants.API_KEY)
            val listing = response.results

            val nextKey = if (listing.isEmpty()) {
                null
            }else{
                position + 1
            }

            val prevKey = if (position == Constants.STARTING_PAGE_INDEX) {
                null
            }else{
                position - 1
            }

            LoadResult.Page(
                data = listing,
                prevKey = prevKey,
                nextKey = nextKey
            )

        } catch (exception: IOException){
            return LoadResult.Error(exception)
        } catch (exception: HttpException){
            return LoadResult.Error(exception)
        }
    }
}