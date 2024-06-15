package com.capstoneproject.`common-adapter`

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.data.model.RecommendationLocation
import com.capstoneproject.databinding.ItemRecommendationLocationBinding

class RecommendationLocationAdapter: RecyclerView.Adapter<RecommendationLocationAdapter.ViewHolder>() {

    private var listLocation = ArrayList<RecommendationLocation>()

    fun setListLocation (newList: List<RecommendationLocation>) {
        listLocation.clear()
        listLocation.addAll(newList)
        notifyDataSetChanged()
    }
    class ViewHolder(var binding: ItemRecommendationLocationBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecommendationLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listLocation.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (_, _, image) = listLocation[position]
        Glide.with(holder.itemView.context)
            .load(image)
            .into(holder.binding.ivLocation)
    }
}