package com.capstoneproject.ui.listads.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.R
import com.capstoneproject.data.model.advertisingcontent.AdvertisingContent
import com.capstoneproject.databinding.ItemListAdsBinding
import com.capstoneproject.ui.listbooking.adapter.ListBookingAdapter

class ListAdsAdapter: RecyclerView.Adapter<ListAdsAdapter.ViewHolder>(), Filterable {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: ItemListAdsBinding): RecyclerView.ViewHolder(binding.root)

    private var listAdvertisingContents = ArrayList<AdvertisingContent>()
    private var filteredList = ArrayList<AdvertisingContent>()

    init {
        filteredList = listAdvertisingContents
    }

    fun setOrdersList(newList: List<AdvertisingContent>) {
        listAdvertisingContents.clear()
        listAdvertisingContents.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListAdsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = filteredList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var advertisingContent = listAdvertisingContents[position]

        holder.binding.apply {
            tvAdsName.text = advertisingContent.name
            tvAdsCategory.text = advertisingContent.category

            Glide.with(holder.itemView)
                .load(advertisingContent.imageUrl)
                .into(ivAdvertisingContent)

            if (advertisingContent.status == "draft") {
                tvStatus.text = "Draft"
                 containerStatus.setBackgroundResource(R.drawable.bg_btn_gray)
            }

            if (advertisingContent.status == "active") {
                tvStatus.text = "Aktif"
                containerStatus.setBackgroundResource(R.drawable.bg_btn_green)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(advertisingContent)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrBlank()) {
                    filterResults.values = listAdvertisingContents
                } else {
                    val query = constraint.toString().trim().lowercase()
                    val filtered = ArrayList<AdvertisingContent>()

                    for (advertisingContent in listAdvertisingContents) {
                        if (advertisingContent.name?.trim()?.lowercase()?.contains(query) == true) {
                            filtered.add(advertisingContent)
                        }
                    }

                    filterResults.values = filtered
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<AdvertisingContent>
                notifyDataSetChanged()
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(advertisingContent: AdvertisingContent)
    }
}