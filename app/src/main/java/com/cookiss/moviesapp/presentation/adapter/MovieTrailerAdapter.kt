package com.cookiss.moviesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookiss.movieapp.domain.model.popular_list.PopularMovies
import com.cookiss.moviesapp.R
import com.cookiss.moviesapp.domain.model.movie_videos.MovieVideos
import com.cookiss.moviesapp.util.Constants

class MovieTrailerAdapter(

    private var context: Context,
    private val itemClickListener: OnItemClickListener

) : RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder>() {
    private val data: MutableList<MovieVideos> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image_list: ImageView = itemView.findViewById(R.id.picture_movies)
        val cl_genre: ConstraintLayout = itemView.findViewById(R.id.cl_genre)

        fun bind(context: Context, movies: MovieVideos, itemClickListener: OnItemClickListener) {

            Glide.with(context)
                .load(Constants.YOUTUBE_THUMBNAIL_URL+ movies.key+"/default.jpg")
                .into(image_list)

            cl_genre.setOnClickListener {
                itemClickListener.onItemClicked(itemView, absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieTrailerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_trailer_item, parent, false)
        return ViewHolder(view)
    }

    fun setData(newListData: List<MovieVideos>?) {
        if (newListData == null) return
        data.clear()
        data.addAll(newListData)
        notifyDataSetChanged()
    }

    fun removeData(){
        data.clear()
        notifyDataSetChanged()
    }

    fun getVideoKey(position: Int) : String{
        return data[position].key
    }


    override fun onBindViewHolder(holder: MovieTrailerAdapter.ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(context, data, itemClickListener)

    }

    override fun getItemCount(): Int {
        return data.size
    }


    interface OnItemClickListener{
        fun onItemClicked(v: View, position: Int)
    }
}