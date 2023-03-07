package com.cookiss.moviesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookiss.movieapp.domain.model.popular_list.PopularMovies
import com.cookiss.moviesapp.R
import com.cookiss.moviesapp.domain.model.reviews.Reviews
import com.cookiss.moviesapp.util.Constants

class MovieListPagingAdapter(

    private val context: Context,
    private val itemClickListener: OnItemClickListener

) :
    PagingDataAdapter<PopularMovies, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<PopularMovies>() {
            override fun areItemsTheSame(oldItem: PopularMovies, newItem: PopularMovies): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: PopularMovies, newItem: PopularMovies): Boolean =
                oldItem.id == newItem.id
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { (holder as? DoggoImageViewHolder)?.bind(context, it, itemClickListener) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DoggoImageViewHolder.getInstance(parent)
    }

    fun getId(position: Int) : Int?{
        return getItem(position)?.id
    }


    /**
     * view holder class
     */
    class DoggoImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            //get instance of the DoggoImageViewHolder
            fun getInstance(parent: ViewGroup): DoggoImageViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.movie_list, parent, false)
                return DoggoImageViewHolder(view)
            }
        }

        private var item: Reviews? = null

        val image_list: ImageView = itemView.findViewById(R.id.picture_movies)
        val cl_genre: ConstraintLayout = itemView.findViewById(R.id.cl_genre)

        fun bind(context: Context, movies: PopularMovies, itemClickListener: OnItemClickListener) {
            //loads image from network using coil extension function

            Glide.with(context)
                .load(Constants.IMAGE_URL+"${movies.poster_path}")
                .into(image_list)

            cl_genre.setOnClickListener {
                itemClickListener.onItemClicked(itemView, absoluteAdapterPosition)
            }

        }

    }

    interface OnItemClickListener{
        fun onItemClicked(v: View, position: Int)
    }
}