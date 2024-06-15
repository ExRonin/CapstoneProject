package com.capstoneproject.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import com.capstoneproject.databinding.FragmentHomeBinding
import com.capstoneproject.ui.home.adapter.ProductAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        productAdapter = ProductAdapter(requireContext())

        binding.rvRecommendationLocation.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = productAdapter
        }

      fetchProducts()
    }
    private fun fetchProducts() = lifecycleScope.launch {
        val token = sharedPreferences.getString("token", "") ?: return@launch
        try {
            val response = RetrofitInstance.api.getProductsByUserPreferences("Bearer $token")
            if (response.isSuccessful) {
                val products = response.body()?.data ?: emptyList()
                productAdapter.setListProduct(products)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error unknown"
                Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}