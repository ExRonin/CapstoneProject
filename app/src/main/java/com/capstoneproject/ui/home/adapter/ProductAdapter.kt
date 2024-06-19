package com.capstoneproject.ui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.R
import com.capstoneproject.data.model.product.Product
import com.capstoneproject.data.model.product.ProductResponse
import com.capstoneproject.databinding.ItemAdvertiseProductBinding
import com.capstoneproject.ui.detailproduct.DetailProductActivity
import com.capstoneproject.utils.formatCurrency

class ProductAdapter() :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val productList = ArrayList<Product>()

    fun setListProduct(list: List<Product>) {
        productList.clear()
        productList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ItemAdvertiseProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.binding.apply {
            tvTitle.text = product.name
            tvType.text = product.type
            tvSize.text = "${product.height}x${product.width}"
            tvPrice.text = "Rp ${formatCurrency(product.price)}"

            Glide.with(
                holder.itemView.context
            ).load(product.imageUrl)
                .into(ivProduct)
        }

        holder.itemView.setOnClickListener {
            if (product != null) {
                onItemClickCallback.onItemClicked(product)
            }
        }
    }

    override fun getItemCount(): Int = productList.size

    inner class ProductViewHolder(var binding: ItemAdvertiseProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(story: Product)
    }
}