package com.cookiss.moviesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.cookiss.movieapp.domain.model.genre_list.Genre
import com.cookiss.moviesapp.R

class GenreListAdapter(

    private var context: Context,
    private val itemClickListener: OnItemClickListener

) : RecyclerView.Adapter<GenreListAdapter.ViewHolder>() {
    private val data: MutableList<Genre> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image_list: TextView = itemView.findViewById(R.id.genre_movies)
        val cl_genre: ConstraintLayout = itemView.findViewById(R.id.cl_genre)

        fun bind(context: Context, genre: Genre, itemClickListener: OnItemClickListener) {

            image_list.text = genre.name

            cl_genre.setOnClickListener {
                itemClickListener.onItemClicked(itemView, absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenreListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.genre_list_item, parent, false)
        return ViewHolder(view)
    }

    fun setData(newListData: List<Genre>?) {
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
        return data[position].name
    }

    override fun onBindViewHolder(holder: GenreListAdapter.ViewHolder, position: Int) {
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