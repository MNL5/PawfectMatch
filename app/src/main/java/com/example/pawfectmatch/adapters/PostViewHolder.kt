package com.example.pawfectmatch.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawfectmatch.R
import com.example.pawfectmatch.data.models.InflatedPost
import com.example.pawfectmatch.data.repositories.PostRepository
import com.example.pawfectmatch.data.repositories.UserRepository
import com.example.pawfectmatch.fragments.comment.CommentsFragment
import com.example.pawfectmatch.utils.BaseAlert
import com.example.pawfectmatch.utils.ImageLoaderViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

class PostViewHolder(
    itemView: View,
    userListener: OnPostItemClickListener?,
    editPostListener: OnPostItemClickListener?,
    pawPostListener: OnPostItemClickListener?,
    fragmentManager: FragmentManager?,
    private val imageLoaderViewModel: ImageLoaderViewModel,
    private val postType: PostType
) : RecyclerView.ViewHolder(itemView) {
    private var layout: ConstraintLayout = itemView.findViewById(R.id.post_row_main)
    private var menu: ImageView = itemView.findViewById(R.id.post_row_menu)
    private var username: TextView = itemView.findViewById(R.id.post_row_username)
    private var animal: TextView = itemView.findViewById(R.id.post_row_animal)
    private var content: TextView = itemView.findViewById(R.id.post_row_content)
    private var animalImage: ImageView = itemView.findViewById(R.id.post_row_animal_image)
    private var avatar: ImageView = itemView.findViewById(R.id.post_row_avatar)
    private var comment: Button = itemView.findViewById(R.id.post_row_comment_button)
    private var paw: Button = itemView.findViewById(R.id.post_row_paw_button)
    private var date: TextView = itemView.findViewById(R.id.date)
    private var progressBarAvatar: View = itemView.findViewById(R.id.progress_bar_avatar)
    private var progressBarAnimal: View = itemView.findViewById(R.id.progress_bar_animal)

    private var post: InflatedPost? = null

    init {
        username.setOnClickListener {
            val post = post
            if (post != null) userListener?.onClickListener(post)
        }
        avatar.setOnClickListener {
            val post = post
            if (post != null) userListener?.onClickListener(post)
        }
        comment.setOnClickListener {
            val postId = post?.id
            if (fragmentManager != null && postId != null) CommentsFragment.display(
                fragmentManager,
                postId
            )
        }

        paw.setOnClickListener {
            val post = post
            if (post != null) pawPostListener?.onClickListener(post)
        }

        menu.setOnClickListener {
            PopupMenu(menu.context, menu).apply {
                setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        val post = post ?: return false
                        when (item.itemId) {
                            R.id.edit_post -> {
                                editPostListener?.onClickListener(post)
                            }

                            R.id.delete_post -> {
                                layout.alpha = 0.4f
                                PostRepository.getInstance().delete(post.id) {
                                    layout.alpha = 1F
                                    BaseAlert(
                                        "Fail",
                                        "Failed to delete post",
                                        itemView.context
                                    ).show()
                                }
                            }

                            else -> return false
                        }
                        return true
                    }
                })
                inflate(R.menu.post_menu)
                show()
            }
        }
    }

    fun bind(post: InflatedPost?, position: Int) {
        layout.alpha = 1F
        this.post = post

        if (post == null) return

        username.text = post.userName
        animal.text = post.animalId
        content.text = post.content

        val lastUpdated = post.lastUpdated
        if (lastUpdated != null) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            date.text = dateFormat.format(Timestamp(Date(lastUpdated)).toDate())
        }

        Glide.with(itemView.context).clear(animalImage)
        progressBarAnimal.visibility = View.VISIBLE
        imageLoaderViewModel.getImageUrl(post.id, PostRepository.getInstance()) {
            if (post.id == this.post?.id) {
                Glide.with(itemView.context)
                    .load(it)
                    .into(animalImage)
                progressBarAnimal.visibility = View.GONE
            }
        }
        if (postType != PostType.PROFILE) {
            Glide.with(itemView.context).clear(avatar)
            progressBarAvatar.visibility = View.VISIBLE
            imageLoaderViewModel.getImageUrl(post.userId, UserRepository.getInstance()) {
                if (post.id == this.post?.id) {
                    Glide.with(itemView.context)
                        .load(it)
                        .into(avatar)
                    progressBarAvatar.visibility = View.GONE
                }
            }
        }

        val activeUser = UserRepository.getInstance().getLoggedUserId()
        if (post.pawsList.contains(activeUser)) {
            paw.backgroundTintList = null
        } else {
            paw.backgroundTintList = ColorStateList.valueOf(Color.BLACK)
        }

        if (post.userId == activeUser) {
            menu.visibility = View.VISIBLE
        } else {
            menu.visibility = View.GONE
        }

        when (postType) {
            PostType.PROFILE -> {
                username.visibility = View.GONE
                avatar.visibility = View.GONE
                progressBarAvatar.visibility = View.GONE

                val constraintSet = ConstraintSet()
                constraintSet.clone(layout)
                constraintSet.connect(
                    R.id.post_row_animal,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START
                )
                constraintSet.applyTo(layout)
            }

            else -> {}
        }
    }
}