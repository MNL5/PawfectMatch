package com.example.pawfectmatch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pawfectmatch.R
import com.example.pawfectmatch.data.models.InflatedComment
import com.example.pawfectmatch.fragments.comment.CommentsViewModel

class CommentsRecyclerAdapter(private var comments: List<InflatedComment>?, private val viewModel: CommentsViewModel) :
    RecyclerView.Adapter<CommentViewHolder>() {

    override fun getItemCount(): Int = comments?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.comment_row,
            parent,
            false
        )
        return CommentViewHolder(itemView, viewModel)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(
            comment = comments?.get(position),
            position = position
        )
    }

    fun updateComments(newComments: List<InflatedComment>) {
        comments = newComments
        notifyDataSetChanged()
    }
}