package com.capstoneproject.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.R
import com.capstoneproject.databinding.ItemSearchResultBinding

class SearchResultsAdapter(private val itemList: List<SearchResultItem>) :
    RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    private var onItemClickListener: ((SearchResultItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (SearchResultItem) -> Unit) {
        onItemClickListener = listener
    }

    class ViewHolder(val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchResultItem, listener: ((SearchResultItem) -> Unit)?) {
            binding.apply {
                icon.setImageResource(item.iconRes)
                title.text = item.title
                subtitle.text = item.subtitle
                itemView.setOnClickListener {
                    listener?.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item, onItemClickListener)
    }

    override fun getItemCount() = itemList.size
}


data class SearchResultItem(val title: String, val subtitle: String, val iconRes: Int = R.drawable.locicon)
