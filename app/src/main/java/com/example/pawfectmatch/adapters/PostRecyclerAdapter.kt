package com.example.pawfectmatch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawfectmatch.R
import com.example.pawfectmatch.data.models.InflatedPost
import com.example.pawfectmatch.utils.ImageLoaderViewModel

enum class PostType {
    REGULAR, PROFILE
}

interface OnPostItemClickListener {
    fun onClickListener(post: InflatedPost)
}

class PostsRecyclerAdapter(
    private var posts: List<InflatedPost>,
    private val imageLoaderViewModel: ImageLoaderViewModel
) :
    RecyclerView.Adapter<PostViewHolder>() {

    var userListener: OnPostItemClickListener? = null
    var editPostListener: OnPostItemClickListener? = null
    var pawPostListener: OnPostItemClickListener? = null
    var fragmentManager: FragmentManager? = null
    var postType = PostType.REGULAR

    override fun getItemCount(): Int = posts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.post_row,
            parent,
            false
        )
        return PostViewHolder(
            itemView,
            userListener,
            editPostListener,
            pawPostListener,
            fragmentManager,
            imageLoaderViewModel,
            postType
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(
            post = posts[position],
            position = position
        )
    }

    fun updatePosts(newPosts: List<InflatedPost>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}