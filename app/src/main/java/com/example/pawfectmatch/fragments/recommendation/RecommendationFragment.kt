package com.example.pawfectmatch.fragments.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.pawfectmatch.R
import com.example.pawfectmatch.adapters.OnPostItemClickListener
import com.example.pawfectmatch.adapters.PostsRecyclerAdapter
import com.example.pawfectmatch.data.models.InflatedPost
import com.example.pawfectmatch.data.repositories.UserRepository
import com.example.pawfectmatch.databinding.FragmentRecommendationBinding
import com.example.pawfectmatch.fragments.post.PostsListFragmentDirections
import com.example.pawfectmatch.utils.BaseAlert

class RecommendationFragment : Fragment() {
    private val viewModel: RecommendationViewModel by viewModels()
    private var binding: FragmentRecommendationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recommendation, container, false
        )
        bindViews()
        setupSearchButton()
        setupRecommendationImage()

        viewModel.initForm()
        return binding?.root
    }

    private fun setupRecommendationImage() {
        viewModel.recommendationAnimalURL.observe(viewLifecycleOwner) {
            binding?.recommendationAnimalImage?.let { imageView -> Glide.with(this).load(it).into(imageView) }
        }
    }

    private fun setupSearchButton() {
        binding?.recommendationSearch?.setOnClickListener({
            viewModel.fetchResponse(
                { error ->
                    BaseAlert(
                        "Fail",
                        if (error == null) "Invalid form" else "Failed to search for recommendation",
                        requireContext()
                    ).show()
                })
        })
    }

    private fun bindViews() {
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}