package com.cookiss.moviesapp.domain.model.reviews

data class Reviews(
    val id: String,
    val author: String,
    val content: String,
    val url: String,
    var isCollapse: Boolean = false,
    var expandable: Boolean = false,
)