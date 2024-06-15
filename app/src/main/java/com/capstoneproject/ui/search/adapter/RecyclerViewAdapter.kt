package com.capstoneproject.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.R
import com.google.android.material.card.MaterialCardView


class RecyclerViewAdapter(private val itemList: List<ItemData>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val card: MaterialCardView = itemView.findViewById(R.id.card)
        val imageView: ImageView = itemView.findViewById(R.id.imagemaps)
        val titlebaliho: TextView = itemView.findViewById(R.id.titlebaliho)
        val title: TextView = itemView.findViewById(R.id.titlecard)
        val size: TextView = itemView.findViewById(R.id.sizesmaps)
        val time: TextView = itemView.findViewById(R.id.timemaps)
        val ratio: TextView = itemView.findViewById(R.id.ratiomaps)
        val views: TextView = itemView.findViewById(R.id.viewmaps)
        val originalPrice: TextView = itemView.findViewById(R.id.original_price)
        val discountedPrice: TextView = itemView.findViewById(R.id.discounted_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_maps_slider, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.imageView.setImageResource(item.imageRes)
        holder.titlebaliho.text = item.titlebaliho
        holder.title.text = item.title
        holder.size.text = item.size
        holder.time.text = item.time
        holder.ratio.text = item.ratio
        holder.views.text = item.views
        holder.originalPrice.text = item.originalPrice
        holder.discountedPrice.text = item.discountedPrice

    }

    override fun getItemCount() = itemList.size
}

data class ItemData(
    val imageRes: Int,
    val titlebaliho: String,
    val title: String,
    val size: String,
    val time: String,
    val ratio: String,
    val views: String,
    val originalPrice: String,
    val discountedPrice: String
)
