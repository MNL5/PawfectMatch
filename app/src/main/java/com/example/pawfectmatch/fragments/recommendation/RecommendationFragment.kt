package com.example.pawfectmatch.fragments.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pawfectmatch.R
import com.example.pawfectmatch.databinding.FragmentRecommendationBinding
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
        setupLoading()
        setupSearchButton()

        viewModel.initForm()
        return binding?.root
    }

    private fun setupSearchButton() {
        binding?.recommendationSearch?.setOnClickListener({
            viewModel.fetchResponse { error ->
                BaseAlert(
                    "Fail",
                    if (error == null) "Invalid form" else "Failed to save post",
                    requireContext()
                ).show()
            }
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

    private fun setupLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) binding?.progressBar?.visibility = View.VISIBLE
            else binding?.progressBar?.visibility = View.INVISIBLE
        }
    }
}