package com.capstoneproject.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.databinding.FragmentSearchResultsBinding
import com.capstoneproject.ui.search.adapter.SearchResultItem
import com.capstoneproject.ui.search.adapter.SearchResultsAdapter


class SearchResultsFragment : Fragment() {

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!


    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private val searchResults = mutableListOf<SearchResultItem>()
    private lateinit var recentSearchesAdapter: SearchResultsAdapter
    private lateinit var recommendationsAdapter: SearchResultsAdapter

    private val recentSearches = listOf(
        SearchResultItem("Videotron Plaza Indonesia", "Jl. M.T. Haryono No. 245, Cikoko, Pancoran, Jakar..."),

    )

    private val recommendations = listOf(
        SearchResultItem("Rekomendasi 1", "Jl. ABC No. 123, Jakarta..."),
        SearchResultItem("Rekomendasi 2", "Jl. DEF No. 456, Jakarta..."),

    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        val view = binding.root

        recentSearchesAdapter = SearchResultsAdapter(recentSearches)
        recommendationsAdapter = SearchResultsAdapter(recommendations)

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

        setupSearchEditText()
        return view
    }
    private fun setupSearchEditText() {
        binding.etSearchResult.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchTerm = binding.etSearchResult.text.toString()
                performSearch(searchTerm)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun performSearch(searchTerm: String) {

        searchResults.clear()


        searchResults.addAll(getDummySearchResults(searchTerm))


        searchResultsAdapter.notifyDataSetChanged()
        binding.recyclerViewRecentSearches.visibility = View.GONE
        binding.recyclerViewRecommendations.visibility = View.GONE
        binding.lasttext.visibility = View.GONE
        binding.rectext.visibility = View.GONE
        binding.recyclerSearchResult.visibility = if (searchResults.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun getDummySearchResults(searchTerm: String): List<SearchResultItem> {

        val dummyResults = mutableListOf<SearchResultItem>()

        for (i in 1..5) {
            dummyResults.add(SearchResultItem("Result $i for '$searchTerm'", "Subtitle $i"))
        }

        return dummyResults
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
