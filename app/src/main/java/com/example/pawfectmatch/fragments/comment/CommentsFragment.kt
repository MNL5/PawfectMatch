package com.example.pawfectmatch.fragments.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawfectmatch.R
import com.example.pawfectmatch.adapters.CommentsRecyclerAdapter
import com.example.pawfectmatch.databinding.FragmentCommentsBinding
import com.example.pawfectmatch.utils.BaseAlert

private const val POST_ID = "postId"
private const val TAG = "comments"

class CommentsFragment : DialogFragment() {
    private var viewModel: CommentsViewModel? = null
    private var binding: FragmentCommentsBinding? = null

    private var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getString(POST_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_comments, container, false
        )

        val postId = postId
        if (postId != null) {
            val viewModel = CommentsViewModel(postId)
            this.viewModel = viewModel
            bindViews()

            binding?.commentsRecyclerView?.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(context)
            binding?.commentsRecyclerView?.layoutManager = layoutManager
            val adapter = CommentsRecyclerAdapter(emptyList(), viewModel)
            binding?.commentsRecyclerView?.adapter = adapter

            viewModel.comments.observe(viewLifecycleOwner) { comments ->
                adapter.updateComments(comments)
            }
        }

        binding?.sendButton?.setOnClickListener {
            viewModel?.submit { _ ->
                BaseAlert("Fail", "Failed to save comment", requireContext()).show()
            }
        }

        binding?.closeButton?.setOnClickListener {
            dismiss()
        }

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            viewModel?.fetchComments()
        }

        viewModel?.isLoading?.observe(viewLifecycleOwner) {
            binding?.swipeRefreshLayout?.isRefreshing = it || viewModel?.isRefreshing?.value == true
        }

        viewModel?.isRefreshing?.observe(viewLifecycleOwner) {
            binding?.swipeRefreshLayout?.isRefreshing = it || viewModel?.isLoading?.value == true
        }

        return binding?.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun bindViews() {
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        fun display(fragmentManager: FragmentManager, postId: String) {
            val fragment = CommentsFragment()
            val args = Bundle()
            args.putString(POST_ID, postId)
            fragment.arguments = args
            fragment.show(fragmentManager, TAG)
        }
    }
}