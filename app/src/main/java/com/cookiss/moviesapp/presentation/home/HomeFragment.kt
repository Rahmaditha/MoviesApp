package com.cookiss.moviesapp.presentation.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cookiss.movieapp.domain.model.genre_list.Genre
import com.cookiss.movieapp.domain.model.popular_list.PopularMovies
import com.cookiss.moviesapp.R
import com.cookiss.moviesapp.databinding.FragmentHomeBinding
import com.cookiss.moviesapp.presentation.adapter.GenreListAdapter
import com.cookiss.moviesapp.presentation.adapter.ImageSlideAdapter
import com.cookiss.moviesapp.util.Constants
import com.cookiss.moviesapp.util.Status
import dagger.hilt.android.AndroidEntryPoint
import me.relex.circleindicator.CircleIndicator

@AndroidEntryPoint
class HomeFragment : Fragment(),
    GenreListAdapter.OnItemClickListener,
        ImageSlideAdapter.OnItemClickListener
{

    private val TAG = this.javaClass.simpleName

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel : HomeViewModel by viewModels()

    private lateinit var rv_genre_list: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var popularMoviesList : MutableList<PopularMovies> = ArrayList()
    private lateinit var genreAdapter: GenreListAdapter

    lateinit var viewPagerAdapter: ImageSlideAdapter
    lateinit var indicator: CircleIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_genre_list = binding.rvGenreList
        initRecyclerView()

        getMoviePopular()
        getGenreList()

        navHostFragment = requireActivity()?.supportFragmentManager?.findFragmentById(R.id.main_nav_host) as NavHostFragment
        navController = navHostFragment.findNavController()
    }


    private fun getMoviePopular() {
        homeViewModel.getPopularMovies()
        homeViewModel.popularMoviesResult.observe(viewLifecycleOwner, Observer { genre_list ->
            when(genre_list.status){
                Status.SUCCESS -> {
                    genre_list._data?.let {

                        val stringUrl : List<String> = it.results.map {
                            Constants.IMAGE_URL + it.poster_path
                        }

                        viewPagerAdapter = ImageSlideAdapter(requireContext(), stringUrl, this)
                        binding.viewpager.adapter = viewPagerAdapter
                        indicator = requireView().findViewById(R.id.indicator) as CircleIndicator
                        indicator.setViewPager(binding.viewpager)

                        popularMoviesList.clear()
                        popularMoviesList.addAll(it.results)
                    }
                    binding.progressBar.visibility =View.GONE

                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    genre_list.message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                    binding.progressBar.visibility = View.GONE

                }
            }
        })
    }

    private fun getGenreList() {
        homeViewModel.getGenreList()
        homeViewModel.genreListResult.observe(viewLifecycleOwner, Observer { genre_list ->
            when(genre_list.status){
                Status.SUCCESS -> {
                    genre_list._data?.let {
                        genreAdapter.removeData()
                        genreAdapter.setData(it.genres)
                    }
                    binding.progressBar.visibility =View.GONE

                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    genre_list.message?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                    binding.progressBar.visibility = View.GONE

                }
            }
        })
    }

    private fun initRecyclerView() {
        genreAdapter = GenreListAdapter(requireContext(), this)
//        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        staggeredGridLayoutManager = object : StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        rv_genre_list.layoutManager = staggeredGridLayoutManager
        rv_genre_list.adapter = genreAdapter
    }

    override fun onItemClicked(v: View, position: Int) {
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToMovieListFragment()
            .setStringIdCategory(genreAdapter.getId(position).toString())
            .setStringCategoryName(genreAdapter.getName(position))
        )
    }

    override fun onImageClicked(position: Int) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment()
            .setStringMovieId(popularMoviesList[position].id.toString()))

    }
}