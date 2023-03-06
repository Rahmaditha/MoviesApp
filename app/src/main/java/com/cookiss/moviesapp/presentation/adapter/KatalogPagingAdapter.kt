package com.cookiss.moviesapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cookiss.moviesapp.R
import com.cookiss.moviesapp.domain.model.reviews.Reviews

class KatalogPagingAdapter(
    private val itemClickListener: OnItemClickListener
) :
    PagingDataAdapter<Reviews, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Reviews>() {
            override fun areItemsTheSame(oldItem: Reviews, newItem: Reviews): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Reviews, newItem: Reviews): Boolean =
                oldItem.id == newItem.id
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { (holder as? DoggoImageViewHolder)?.bind(it, itemClickListener) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DoggoImageViewHolder.getInstance(parent)
    }

    fun getId(position: Int) : String?{
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
                val view = inflater.inflate(R.layout.review_list_item, parent, false)
                return DoggoImageViewHolder(view)
            }
        }

        private var item: Reviews? = null

        val username: TextView = itemView.findViewById(R.id.user_name)
        val review: TextView = itemView.findViewById(R.id.review)
        val cl_genre: ConstraintLayout = itemView.findViewById(R.id.cl_genre)

        fun bind(reviews: Reviews, itemClickListener: OnItemClickListener) {
            //loads image from network using coil extension function

            username.text = reviews.author
            review.text = reviews.content

            cl_genre.setOnClickListener {
                itemClickListener.onShowMoreClicked(itemView, absoluteAdapterPosition)
            }

        }

    }

    interface OnItemClickListener{
        fun onShowMoreClicked(v: View, position: Int)
    }
}