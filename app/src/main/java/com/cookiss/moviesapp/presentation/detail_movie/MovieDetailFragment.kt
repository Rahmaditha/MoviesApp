package com.cookiss.moviesapp.presentation.detail_movie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.cookiss.movieapp.domain.model.genre_list.Genre
import com.cookiss.moviesapp.databinding.FragmentMovieDetailBinding
import com.cookiss.moviesapp.domain.model.reviews.Reviews
import com.cookiss.moviesapp.presentation.adapter.KatalogPagingAdapter
import com.cookiss.moviesapp.presentation.adapter.MovieTrailerAdapter
import com.cookiss.moviesapp.presentation.adapter.LoadingAdapter
import com.cookiss.moviesapp.presentation.home.HomeViewModel
import com.cookiss.moviesapp.util.Constants
import com.cookiss.moviesapp.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment(), MovieTrailerAdapter.OnItemClickListener, KatalogPagingAdapter.OnItemClickListener {

    private val TAG = this.javaClass.simpleName

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel : HomeViewModel by viewModels()

    private lateinit var rv_trailer_list: RecyclerView
    private var katalogList : MutableList<Reviews> = ArrayList()

    private lateinit var rv_reviews: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManager2: LinearLayoutManager

    private var genreList : MutableList<Genre> = ArrayList()
    private lateinit var movieTrailerAdapter: MovieTrailerAdapter
    private lateinit var reviewAdapter: KatalogPagingAdapter
    private val args: MovieDetailFragmentArgs by navArgs()

    private var movieId : String = ""
    private var page : Int = 1

    private var visibleItemCount : Int = 1
    private var totalItemCount : Int = 1
    private var pastVisibleItem : Int = 1

    private var total_pages : Int = 0
    private var isLoading : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_trailer_list = binding.rvVideos
        rv_reviews = binding.rvReviews

        movieId = args.stringMovieId
        initRecyclerView()
        initRecyclerViewReviews()

        getMovieDetail(movieId)
        getMovieVideos(movieId)
        fetchReviews(movieId)
//        getMovieReviews(page, movieId)

        binding.clBack.setOnClickListener {
            findNavController().popBackStack()

        }
    }

    private fun getMovieDetail(movieId: String) {
        homeViewModel.getMovieDetail(movieId)
        homeViewModel.movieDetailResult.observe(viewLifecycleOwner, Observer { movie_list ->
            when(movie_list.status){
                Status.SUCCESS -> {
                    movie_list._data?.let {movie ->

                        Glide.with(requireContext())
                            .load(Constants.IMAGE_URL + movie.poster_path)
                            .into(binding.ivMovie)

                        binding.tvTitle.text = movie.title
                        binding.tvReleaseDate.text = "Released Date: ${movie.release_date}"
                        binding.tvSummary.text = movie.overview
                        binding.ratingBar.rating = movie.vote_average.toFloat() / 2f
                    }
                    binding.progressBar.visibility =View.GONE

                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    movie_list.message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                    binding.progressBar.visibility = View.GONE

                }
            }
        })
    }

    private fun getMovieVideos(movieId: String) {
        homeViewModel.getMovieVideos(movieId)
        homeViewModel.movieVideosResult.observe(viewLifecycleOwner, Observer { movie_list ->
            when(movie_list.status){
                Status.SUCCESS -> {
                    movie_list._data?.let {movie ->
                        movieTrailerAdapter.removeData()
                        movieTrailerAdapter.setData(movie.results)
                    }
                    binding.progressBar.visibility =View.GONE

                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    movie_list.message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                    binding.progressBar.visibility = View.GONE

                }
            }
        })


    }

//    private fun getMovieReviews(page: Int, movieId: String) {
//        homeViewModel.getReviews(page, movieId)
//        homeViewModel.movieReviewsResult.observe(viewLifecycleOwner, Observer { movie_list ->
//            when(movie_list.status){
//                Status.SUCCESS -> {
//                    movie_list._data?.let {movie ->
//
//                        isLoading = true
//                        if(page.toInt() == 1){
//                            reviewAdapter.removeData()
//                            reviewAdapter.setData(movie.results)
//                        }
//                        else{
//                            reviewAdapter.addData(movie.results)
//
//                        }
//
//                        total_pages = movie.total_pages
//
//
//                    }
//                    binding.progressBar.visibility =View.GONE
//
//                }
//                Status.LOADING -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//                Status.ERROR -> {
//                    movie_list.message?.let {
//                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
//                    }
//                    binding.progressBar.visibility = View.GONE
//
//                }
//            }
//        })
//    }

    private fun fetchReviews(movieId: String) {
        lifecycleScope.launch {
            homeViewModel.fetchReview(movieId).collectLatest { pagingData ->
                Log.d(TAG, "fetchReviews: $pagingData")

                reviewAdapter.submitData(pagingData)
                katalogList.addAll(reviewAdapter.snapshot().items)

                Log.d(TAG, "fetchReviews: $katalogList")
            }
        }
    }

    private fun initRecyclerView() {
        movieTrailerAdapter = MovieTrailerAdapter(requireContext(), this)
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_trailer_list.layoutManager = linearLayoutManager
        rv_trailer_list.adapter = movieTrailerAdapter
    }

    private fun initRecyclerViewReviews() {
        reviewAdapter = KatalogPagingAdapter(this)
        linearLayoutManager2 = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_reviews.layoutManager = linearLayoutManager2
        rv_reviews.adapter = reviewAdapter

        rv_reviews.adapter = reviewAdapter.withLoadStateHeaderAndFooter(
            header = LoadingAdapter { reviewAdapter.retry() },
            footer = LoadingAdapter { reviewAdapter.retry() }
        )
    }

    override fun onItemClicked(v: View, position: Int) {

        val playVideoIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_VIDEO_URL + movieTrailerAdapter.getVideoKey(position)))
        requireContext().startActivity(playVideoIntent)
    }

    override fun onShowMoreClicked(v: View, position: Int) {

    }

}