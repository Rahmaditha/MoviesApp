package com.cookiss.moviesapp.data.remote

import androidx.paging.PagingSource
import com.cookiss.moviesapp.domain.model.reviews.Reviews
import com.cookiss.moviesapp.domain.model.reviews.ReviewsResponse
import com.cookiss.moviesapp.util.Constants
import com.cookiss.moviesapp.util.Constants.STARTING_PAGE_INDEX
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ReviewPagingSource @Inject constructor(
    private val apiService: ApiService,
    private val movieId: String
) : PagingSource<Int, Reviews>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Reviews> {

        val position = params.key ?: STARTING_PAGE_INDEX


        return try {
            val response = apiService.getReviews(movieId, position, Constants.API_KEY)
            val listing = response.results

            val nextKey = if (listing.isEmpty()) {
                null
            }else{
                position + 1
            }

            val prevKey = if (position == STARTING_PAGE_INDEX) {
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