package com.capstoneproject.ui.listbooking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.R
import com.capstoneproject.data.model.order.Order
import com.capstoneproject.databinding.ItemListBookingBinding
import com.capstoneproject.utils.calculateDaysBetween
import com.capstoneproject.utils.formatCurrency
import com.capstoneproject.utils.gone
import com.capstoneproject.utils.visible

class ListBookingAdapter() : RecyclerView.Adapter<ListBookingAdapter.ViewHolder>(), Filterable {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: ItemListBookingBinding) : RecyclerView.ViewHolder(binding.root)

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
            ItemListBookingBinding.inflate(
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
            tvProductBookingDate.text = "${order.startBooked} s/d ${order.endBooked}"
            tvTotalPayment.text = "Rp ${formatCurrency(order.totalPayment?.toInt() ?: 0)}"
            tvBookingDate.text = "13 Jun 2024, 14:00"

            Glide.with(holder.itemView)
                .load(order.imageProduct)
                .into(ivProduct)

            if (order.status != "pending") {
                btnCompletePayment.gone()
            }

            if (order.status == "pending") {
                containerStatus.setBackgroundResource(R.drawable.bg_btn_blue)
                tvStatus.setText(R.string.pembayaran)
            }

            if (order.status == "paid" || order.status == "approve" || order.status == "rejected" || order.status == "active"  || order.status == "ended") {
                containerStatus.setBackgroundResource(R.drawable.bg_btn_green)
                tvStatus.setText(R.string.lunas)
            }

            if (order.status == "canceled") {
                containerStatus.setBackgroundResource(R.drawable.bg_btn_red)
                tvStatus.setText(R.string.dibatalkan)
            }
        }

        holder.itemView.setOnClickListener {
            if (order != null) {
                onItemClickCallback.onItemClicked(order)
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