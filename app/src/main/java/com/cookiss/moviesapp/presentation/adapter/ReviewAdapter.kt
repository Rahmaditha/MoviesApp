package com.cookiss.moviesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.cookiss.movieapp.domain.model.genre_list.Genre
import com.cookiss.moviesapp.R
import com.cookiss.moviesapp.domain.model.reviews.Reviews
import com.cookiss.moviesapp.util.ExpandableTextView.collapse
import com.cookiss.moviesapp.util.ExpandableTextView.expand

class ReviewAdapter(

    private var context: Context,
    private val itemClickListener: OnItemClickListener


) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    private val data: MutableList<Reviews> = mutableListOf()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val username: TextView = itemView.findViewById(R.id.user_name)
        val review: TextView = itemView.findViewById(R.id.review)
        val tv_show: TextView = itemView.findViewById(R.id.tv_show)
        val cl_genre: ConstraintLayout = itemView.findViewById(R.id.cl_genre)
        val cl_show_more: ConstraintLayout = itemView.findViewById(R.id.cl_show_more)

        fun bind(context: Context, reviews: Reviews, itemClickListener: ReviewAdapter.OnItemClickListener) {

            username.text = reviews.author
            review.text = reviews.content

            cl_genre.setOnClickListener {
                itemClickListener.onShowMoreClicked(itemView, absoluteAdapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_list_item, parent, false)
        return ViewHolder(view)
    }

    fun setData(newListData: List<Reviews>?) {
        if (newListData == null) return
        data.clear()
        data.addAll(newListData)
        notifyDataSetChanged()
    }

    fun addData(newListData: List<Reviews>?) {
        if (newListData == null) return
        data.addAll(newListData)
        notifyDataSetChanged()
    }

    fun removeData(){
        data.clear()
        notifyDataSetChanged()
    }

    fun getIsCollapse(position: Int) : Boolean{
        return data[position].isCollapse
    }

    fun setExpand(expandable: Boolean, position: Int){
        data[position].expandable = expandable

        notifyDataSetChanged()
    }



    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int) {
        val data = data[position]
        holder.bind(context, data, itemClickListener)

    }

    override fun getItemCount(): Int {
        return data.size
    }


    interface OnItemClickListener{
        fun onShowMoreClicked(v: View, position: Int)
    }
}