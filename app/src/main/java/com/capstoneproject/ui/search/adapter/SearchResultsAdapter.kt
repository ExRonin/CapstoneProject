package com.capstoneproject.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.R
import com.capstoneproject.databinding.ItemSearchResultBinding

class SearchResultsAdapter(private val itemList: List<SearchResultItem>) :
    RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.apply {
            icon.setImageResource(R.drawable.locicon)
            title.text = item.title
            subtitle.text = item.subtitle
        }
    }

    override fun getItemCount() = itemList.size
}

data class SearchResultItem(val title: String, val subtitle: String, val iconRes: Int = R.drawable.locicon)
