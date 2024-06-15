package com.capstoneproject.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.`common-adapter`.RecommendationLocationAdapter
import com.capstoneproject.data.dummy.RecommendationLocationData
import com.capstoneproject.data.model.RecommendationLocation
import com.capstoneproject.data.model.product.Product
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import com.capstoneproject.databinding.FragmentHomeBinding
import com.capstoneproject.ui.detailproduct.DetailProductActivity
import com.capstoneproject.ui.home.adapter.ProductAdapter
import com.capstoneproject.utils.gone
import com.capstoneproject.utils.visible
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recommendationLocationAdapter: RecommendationLocationAdapter
    private var list: ArrayList<RecommendationLocation> = arrayListOf()
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
        recommendationLocationAdapter = RecommendationLocationAdapter()
        productAdapter = ProductAdapter()

        list.addAll(RecommendationLocationData.listData)


        fetchProducts()
        setRecommLocationRV()
        onAction()
    }

    private fun onAction() {
        productAdapter.setOnItemClickCallback(object : ProductAdapter.OnItemClickCallback{
            override fun onItemClicked(product: Product) {
                val intent = Intent(requireContext(), DetailProductActivity::class.java)
                intent.putExtra("extra_product", product)
                startActivity(intent)
            }
        })
    }

    private fun setRecommLocationRV() {
        recommendationLocationAdapter.setListLocation(list)
        binding.rvRecommendationLocation.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendationLocationAdapter
        }
    }

    private fun fetchProducts() = lifecycleScope.launch {
        val token = sharedPreferences.getString("token", "") ?: return@launch
        try {
            loadingAction()
            val response = RetrofitInstance.api.getProductsByUserPreferences("Bearer $token")
            if (response.isSuccessful) {
                val products = response.body()?.data ?: emptyList()
                productAdapter.setListProduct(products)
                binding.rvRecommendationProducts.apply {
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    adapter = productAdapter
                }
                successAction()
            } else {
                errorAction()
                val errorMessage = response.errorBody()?.string() ?: "Error unknown"
                Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            errorAction()
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun errorAction() {
        binding.apply {
            viewShimmer.stopShimmerAnimation()
            viewShimmer.gone()
            rvRecommendationProducts.gone()
        }
    }

    private fun successAction() {
        binding.apply {
            viewShimmer.stopShimmerAnimation()
            viewShimmer.gone()
            rvRecommendationProducts.visible()
        }
    }

    private fun loadingAction() {
        binding.apply {
            rvRecommendationProducts.gone()
            viewShimmer.startShimmerAnimation()
            viewShimmer.visible()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}