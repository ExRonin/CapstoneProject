package com.capstoneproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.adapter.RecommendationLocationAdapter
import com.capstoneproject.data.dummy.RecommendationLocationData
import com.capstoneproject.data.model.RecommendationLocation
import com.capstoneproject.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendationLocationAdapter: RecommendationLocationAdapter
    private var list: ArrayList<RecommendationLocation> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        recommendationLocationAdapter = RecommendationLocationAdapter()

        list.addAll(RecommendationLocationData.listData)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecommLocationRV()
    }

    private fun setRecommLocationRV() {
        recommendationLocationAdapter.setListLocation(list)
        binding.rvRecommendationLocation.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendationLocationAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}