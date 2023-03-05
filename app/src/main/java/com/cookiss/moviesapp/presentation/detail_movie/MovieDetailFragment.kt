package com.cookiss.moviesapp.presentation.detail_movie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import com.cookiss.moviesapp.presentation.adapter.MovieTrailerAdapter
import com.cookiss.moviesapp.presentation.home.HomeViewModel
import com.cookiss.moviesapp.util.Constants
import com.cookiss.moviesapp.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment(), MovieTrailerAdapter.OnItemClickListener {

    private val TAG = this.javaClass.simpleName

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel : HomeViewModel by viewModels()

    private lateinit var rv_trailer_list: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var genreList : MutableList<Genre> = ArrayList()
    private lateinit var movieTrailerAdapter: MovieTrailerAdapter
    private val args: MovieDetailFragmentArgs by navArgs()

    private var movieId : String = ""

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
        movieId = args.stringMovieId
        initRecyclerView()

        getMovieDetail(movieId)
        getMovieVideos(movieId)

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

    private fun initRecyclerView() {
        movieTrailerAdapter = MovieTrailerAdapter(requireContext(), this)
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_trailer_list.layoutManager = linearLayoutManager
        rv_trailer_list.adapter = movieTrailerAdapter
    }

    override fun onItemClicked(v: View, position: Int) {

        val playVideoIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_VIDEO_URL + movieTrailerAdapter.getVideoKey(position)))
        requireContext().startActivity(playVideoIntent)
    }

}