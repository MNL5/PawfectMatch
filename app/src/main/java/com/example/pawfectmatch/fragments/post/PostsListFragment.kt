package com.example.pawfectmatch.fragments.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pawfectmatch.R
import com.example.pawfectmatch.adapters.OnPostItemClickListener
import com.example.pawfectmatch.adapters.PaddedItemDecoration
import com.example.pawfectmatch.adapters.PostsRecyclerAdapter
import com.example.pawfectmatch.data.models.InflatedPost
import com.example.pawfectmatch.data.repositories.UserRepository
import com.example.pawfectmatch.databinding.FragmentPostsListBinding

class PostsListFragment : Fragment() {
    private val viewModel: PostsListViewModel by viewModels()
    private var binding: FragmentPostsListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_posts_list, container, false
        )
        bindViews()
        setupList()

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            viewModel.fetchPosts()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding?.swipeRefreshLayout?.isRefreshing = it
        }

        return binding?.root
    }

    private fun bindViews() {
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setupList() {
        binding?.postsRecyclerView?.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        binding?.postsRecyclerView?.addItemDecoration(PaddedItemDecoration())
        binding?.postsRecyclerView?.layoutManager = layoutManager
        val adapter = PostsRecyclerAdapter(emptyList(), viewModel)
        binding?.postsRecyclerView?.adapter = adapter

        adapter.fragmentManager = getChildFragmentManager()
        adapter.userListener = object : OnPostItemClickListener {
            override fun onClickListener(post: InflatedPost) {
                val action =
                    PostsListFragmentDirections.actionGlobalUserPageFragment(post.userId)
                findNavController().navigate(action)
            }
        }

        adapter.pawPostListener = object : OnPostItemClickListener {
            override fun onClickListener(post: InflatedPost) {
                val loginUser = UserRepository.getInstance().getLoggedUserId()
                if (loginUser != null) {
                    if (post.pawsList.contains(loginUser))
                        viewModel.dispawPost(loginUser, post.id)
                    else viewModel.pawPost(loginUser, post.id)
                }
            }
        }

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            adapter.updatePosts(posts)
        }
    }
}