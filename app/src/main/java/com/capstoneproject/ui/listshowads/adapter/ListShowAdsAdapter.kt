package com.capstoneproject.ui.listshowads.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.R
import com.capstoneproject.data.model.order.Order
import com.capstoneproject.databinding.ItemListShowAdsBinding

class ListShowAdsAdapter: RecyclerView.Adapter<ListShowAdsAdapter.ViewHolder>(), Filterable {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: ItemListShowAdsBinding): RecyclerView.ViewHolder(binding.root)

    private var listOrders = ArrayList<Order>()
    private var filteredList = ArrayList<Order>()

    init {
        filteredList = listOrders
    }

    fun setOrdersList(newList: List<Order>) {
        listOrders.clear()
        listOrders.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListShowAdsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = filteredList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var order = filteredList[position]
        holder.binding.apply {
            tvProductName.text = order.productName
            tvAdsDate.text = "${order.startBooked} s/d ${order.endBooked}"
            tvAdsLocation.text = order.locationProduct
            tvProductCategory.text = order.categoryProduct

            Glide.with(holder.itemView)
                .load(order.imageProduct)
                .into(ivProduct)

            if (order.status == "approve") {
                tvStatus.text = "Disetujui"
                containerStatus.setBackgroundResource(R.drawable.bg_btn_blue)
            }

            if (order.status == "active") {
                tvStatus.text = "Aktif"
                containerStatus.setBackgroundResource(R.drawable.bg_btn_green)
            }

            if (order.status == "rejected") {
                tvStatus.text = "Ditolak"
                containerStatus.setBackgroundResource(R.drawable.bg_btn_red)
            }

            if (order.status == "ended") {
                tvStatus.text = "Berakhir"
                containerStatus.setBackgroundResource(R.drawable.bg_btn_gray)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrBlank()) {
                    filterResults.values = listOrders
                } else {
                    // Lakukan pencarian
                    val query = constraint.toString().trim().lowercase()
                    val filtered = ArrayList<Order>()

                    for (order in listOrders) {
                        if (order.productName?.trim()?.lowercase()?.contains(query) == true) {
                            filtered.add(order)
                        }
                    }

                    filterResults.values = filtered
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<Order>
                notifyDataSetChanged()
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(story: Order)
    }

}