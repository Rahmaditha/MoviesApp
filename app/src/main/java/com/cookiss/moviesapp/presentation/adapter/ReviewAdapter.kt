package com.cookiss.moviesapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cookiss.moviesapp.R
import com.cookiss.moviesapp.domain.model.reviews.Reviews

class ReviewAdapter(

    private var context: Context,
    private val itemClickListener: OnItemClickListener


) : PagingDataAdapter<Reviews, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Reviews>() {
            override fun areItemsTheSame(oldItem: Reviews, newItem: Reviews): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Reviews, newItem: Reviews): Boolean =
                oldItem.id == newItem.id
        }

    }
    private val data: MutableList<Reviews> = mutableListOf()

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        companion object {
            //get instance of the DoggoImageViewHolder
            fun getInstance(parent: ViewGroup): ReviewViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.review_list_item, parent, false)
                return ReviewViewHolder(view)
            }
        }

        val username: TextView = itemView.findViewById(R.id.user_name)
        val review: TextView = itemView.findViewById(R.id.review)
        val cl_genre: ConstraintLayout = itemView.findViewById(R.id.cl_genre)

        fun bind(reviews: Reviews, itemClickListener: OnItemClickListener) {

            username.text = reviews.author
            review.text = reviews.content

            cl_genre.setOnClickListener {
                itemClickListener.onShowMoreClicked(itemView, absoluteAdapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewViewHolder.getInstance(parent)
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



    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as? ReviewViewHolder)?.bind(it, itemClickListener)
        }
    }

    interface OnItemClickListener{
        fun onShowMoreClicked(v: View, position: Int)
    }
}