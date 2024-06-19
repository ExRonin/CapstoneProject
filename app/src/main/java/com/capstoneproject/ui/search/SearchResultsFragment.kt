package com.capstoneproject.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.R
import com.capstoneproject.data.model.product.Product
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import com.capstoneproject.databinding.FragmentSearchResultsBinding
import com.capstoneproject.ui.detailproduct.DetailProductActivity
import com.capstoneproject.ui.home.adapter.ProductAdapter
import com.capstoneproject.ui.search.adapter.RecyclerViewAdapter
import com.capstoneproject.ui.search.adapter.SearchResultItem
import com.capstoneproject.ui.search.adapter.SearchResultsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class SearchResultsFragment : Fragment() {

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private val searchResults = mutableListOf<SearchResultItem>()
    private lateinit var recentSearchesAdapter: SearchResultsAdapter
    private lateinit var recommendationsAdapter: SearchResultsAdapter
    private lateinit var bottomNavigationView: BottomNavigationView


    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        val view = binding.root

        bottomNavigationView = view.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.menu.findItem(R.id.filterFragment).isChecked = false
        bottomNavigationView.menu.findItem(R.id.hargaFragment).isChecked = false
        bottomNavigationView.menu.findItem(R.id.urutkanFragment).isChecked = false
        bottomNavigationView.menu.findItem(R.id.bandingkanFragment).isChecked = false

        setupBottomNavigation()

        binding.backbtn.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        searchResultsAdapter = SearchResultsAdapter(searchResults)
        recentSearchesAdapter = SearchResultsAdapter(getRecentSearches())
        recommendationsAdapter = SearchResultsAdapter(emptyList())

        binding.recyclerViewRecentSearches.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recentSearchesAdapter
        }

        binding.recyclerViewRecommendations.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recommendationsAdapter
        }

        searchResultsAdapter = SearchResultsAdapter(searchResults)

        binding.recyclerSearchResult.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchResultsAdapter
        }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerView.adapter = recyclerViewAdapter

        setupRecyclerViewClickListener()
        setupSearchEditText()


        fetchProducts()
        return view
    }


    private fun fetchProducts() = lifecycleScope.launch {
        val token = sharedPreferences.getString("token", null)
        if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Authorization token is missing", Toast.LENGTH_SHORT).show()
            return@launch
        }

        try {
            val response = RetrofitInstance.api.getProductsByUserPreferences("Bearer $token")
            if (response.isSuccessful) {
                val products = response.body()?.data ?: emptyList()
                updateRecommendations(products)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateRecommendations(products: List<Product>) {
        val items = products.map { product ->
            SearchResultItem(product.name, product.description)
        }
        recommendationsAdapter.updateData(items)
    }


    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.filterFragment -> {
                    findNavController().navigate(R.id.action_searchResultsFragment_to_filterFragment)
                    true
                }
                R.id.hargaFragment -> {
                    findNavController().navigate(R.id.action_searchResultsFragment_to_hargaFragment)
                    true
                }
//                R.id.urutkanFragment -> {
//                    findNavController().navigate(R.id.action_searchResultsFragment_to_urutkanFragment)
//                    true
//                }
//                R.id.bandingkanFragment -> {
//                    findNavController().navigate(R.id.action_searchResultsFragment_to_bandingkanFragment)
//                    true
//                }
                else -> false
            }
        }
    }
    private fun setupRecyclerViewClickListener() {
        recentSearchesAdapter.setOnItemClickListener {
//            binding.mapView.visibility = View.VISIBLE
//            binding.bottomNavigationView.visibility = View.VISIBLE
//            binding.recyclerView.visibility = View.VISIBLE
//            binding.recyclerViewRecentSearches.visibility = View.GONE
//            binding.recyclerViewRecommendations.visibility = View.GONE
//            binding.recyclerSearchResult.visibility = View.GONE
//            binding.lasttext.visibility = View.GONE
//            binding.rectext.visibility = View.GONE
            showUnderMaintenanceToast()
        }

        recommendationsAdapter.setOnItemClickListener {
//            binding.mapView.visibility = View.VISIBLE
//            binding.bottomNavigationView.visibility = View.VISIBLE
//            binding.recyclerView.visibility = View.VISIBLE
//            binding.recyclerViewRecentSearches.visibility = View.GONE
//            binding.recyclerViewRecommendations.visibility = View.GONE
//            binding.recyclerSearchResult.visibility = View.GONE
//            binding.lasttext.visibility = View.GONE
//            binding.rectext.visibility = View.GONE
            showUnderMaintenanceToast()
        }
        searchResultsAdapter.setOnItemClickListener {
//            binding.mapView.visibility = View.VISIBLE
//            binding.bottomNavigationView.visibility = View.VISIBLE
//            binding.recyclerView.visibility = View.VISIBLE
//            binding.recyclerViewRecentSearches.visibility = View.GONE
//            binding.recyclerViewRecommendations.visibility = View.GONE
//            binding.recyclerSearchResult.visibility = View.GONE
//            binding.lasttext.visibility = View.GONE
//            binding.rectext.visibility = View.GONE
            showUnderMaintenanceToast()
        }
    }

    private fun showUnderMaintenanceToast() {
        Toast.makeText(requireContext(), "This feature is under maintenance", Toast.LENGTH_SHORT).show()
    }
    private fun setupSearchEditText() {
        binding.etSearchResult.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.etSearchResult.text.toString()
                performSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun performSearch() {
        searchResults.clear()
        saveRecentSearch()
        fetchSearchResults()
    }

    private fun fetchSearchResults() = lifecycleScope.launch {
        val token = sharedPreferences.getString("token", null)
        if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Authorization token is missing", Toast.LENGTH_SHORT).show()
            return@launch
        }

        try {
            val response = RetrofitInstance.api.getProductsByUserPreferences("Bearer $token")
            if (response.isSuccessful) {
                val products = response.body()?.data ?: emptyList()
                val items = products.map { product ->
                    SearchResultItem(product.name, product.description)
                }
                searchResults.addAll(items)
                searchResultsAdapter.notifyDataSetChanged()
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerViewRecentSearches.visibility = View.GONE
        binding.recyclerViewRecommendations.visibility = View.GONE
        binding.lasttext.visibility = View.GONE
        binding.rectext.visibility = View.GONE
        binding.recyclerSearchResult.visibility = if (searchResults.isEmpty()) View.GONE else View.VISIBLE
    }


    private fun saveRecentSearch() {
        val recentSearches = getRecentSearches().toMutableList()
        if (recentSearches.size >= 10) {
            recentSearches.removeAt(recentSearches.size - 1)
        }
        recentSearches.add(0, SearchResultItem(binding.etSearchResult.text.toString(), ""))

        val recentSearchesJson = Gson().toJson(recentSearches)
        sharedPreferences.edit().putString("recent_searches", recentSearchesJson).apply()

        recentSearchesAdapter.updateData(recentSearches)
    }

    private fun getRecentSearches(): List<SearchResultItem> {
        val recentSearchesJson = sharedPreferences.getString("recent_searches", null)
        return if (recentSearchesJson != null) {
            val type = object : TypeToken<List<SearchResultItem>>() {}.type
            Gson().fromJson(recentSearchesJson, type)
        } else {
            emptyList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}