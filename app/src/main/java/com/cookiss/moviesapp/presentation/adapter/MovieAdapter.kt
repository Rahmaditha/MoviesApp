package com.cookiss.moviesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookiss.movieapp.domain.model.genre_list.Genre
import com.cookiss.movieapp.domain.model.popular_list.PopularMovies
import com.cookiss.moviesapp.R
import com.cookiss.moviesapp.util.Constants

class MovieAdapter(

    private var context: Context,
    private val itemClickListener: OnItemClickListener

) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private val data: MutableList<PopularMovies> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image_list: ImageView = itemView.findViewById(R.id.picture_movies)
        val cl_genre: ConstraintLayout = itemView.findViewById(R.id.cl_genre)

        fun bind(context: Context, movies: PopularMovies, itemClickListener: OnItemClickListener) {

            Glide.with(context)
                .load(Constants.IMAGE_URL+"${movies.poster_path}")
                .into(image_list)

            cl_genre.setOnClickListener {
                itemClickListener.onItemClicked(itemView, absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list, parent, false)
        return ViewHolder(view)
    }

    fun setData(newListData: List<PopularMovies>?) {
        if (newListData == null) return
        data.clear()
        data.addAll(newListData)
        notifyDataSetChanged()
    }

    fun removeData(){
        data.clear()
        notifyDataSetChanged()
    }

    fun getId(position: Int) : Int{
        return data[position].id
    }

    fun getName(position: Int) : String{
        return data[position].poster_path
    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
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