package com.cookiss.moviesapp.presentation.movie_list

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
import com.cookiss.movieapp.domain.model.genre_list.Genre
import com.cookiss.moviesapp.databinding.FragmentMovieListBinding
import com.cookiss.moviesapp.presentation.adapter.LoadingAdapter
import com.cookiss.moviesapp.presentation.adapter.MovieAdapter
import com.cookiss.moviesapp.presentation.adapter.MovieListPagingAdapter
import com.cookiss.moviesapp.presentation.home.HomeFragmentDirections
import com.cookiss.moviesapp.presentation.home.HomeViewModel
import com.cookiss.moviesapp.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment(), MovieListPagingAdapter.OnItemClickListener {

    private val TAG = this.javaClass.simpleName

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel : HomeViewModel by viewModels()

    private lateinit var rv_movie_list: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var genreList : MutableList<Genre> = ArrayList()
    private lateinit var movieAdapter: MovieListPagingAdapter
    private val args: MovieListFragmentArgs by navArgs()

    private var genreId : String = ""
    private var genreName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieListBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_movie_list = binding.rvMovieList
        initRecyclerView()

        genreId = args.stringIdCategory
        genreName = args.stringCategoryName

//        getMovieList(genreId)
        fetchMovies(genreId)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun fetchMovies(with_genre: String) {
        lifecycleScope.launch {
            homeViewModel.fetchMovie(with_genre).collectLatest { pagingData ->
                Log.d(TAG, "fetchMovies: $pagingData")

                movieAdapter.submitData(pagingData)
            }
        }
    }

    private fun getMovieList(genreId: String) {
        homeViewModel.getMoviesByGenre(genreId)
        homeViewModel.popularMoviesResult.observe(viewLifecycleOwner, Observer { movie_list ->
            when(movie_list.status){
                Status.SUCCESS -> {
                    movie_list._data?.let {
//                        movieAdapter.removeData()
//                        movieAdapter.setData(it.results)

                        binding.categories.text = genreName
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
        movieAdapter = MovieListPagingAdapter(requireContext(), this)
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_movie_list.layoutManager = staggeredGridLayoutManager
        rv_movie_list.adapter = movieAdapter

        rv_movie_list.adapter = movieAdapter.withLoadStateHeaderAndFooter(
            header = LoadingAdapter { movieAdapter.retry() },
            footer = LoadingAdapter { movieAdapter.retry() }
        )
    }

    override fun onItemClicked(v: View, position: Int) {
        findNavController().navigate(
            MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment()
            .setStringMovieId(movieAdapter.getId(position).toString())
        )
    }
}