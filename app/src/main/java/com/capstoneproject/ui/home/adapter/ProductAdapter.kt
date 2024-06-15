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
import com.capstoneproject.ui.detailproduct.DetailProductActivity

class ProductAdapter(private val context: Context) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val productList = ArrayList<Product>()

    fun setListProduct(list: List<Product>) {
        productList.clear()
        productList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommendation_location, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_location)
        private val textView: TextView = itemView.findViewById(R.id.tv_text)

        fun bind(product: Product) {
            Glide.with(itemView.context).load(product.imageUrl).into(imageView)
            textView.text = product.name

            itemView.setOnClickListener {
                val intent = Intent(context, DetailProductActivity::class.java).apply {
                    putExtra("id", product.id)
                    putExtra("endBooked", product.endBooked)
                    putExtra("latitude", product.latitude)
                    putExtra("isBooked", product.isBooked)
                    putExtra("type", product.type)
                    putExtra("userId", product.userId)
                    putExtra("startBooked", product.startBooked)
                    putExtra("userImage", product.userImage)
                    putExtra("price", product.price)
                    putExtra("imageUrl", product.imageUrl)
                    putExtra("userPosition", product.userPosition)
                    putExtra("name", product.name)
                    putExtra("width", product.width)
                    putExtra("userFullname", product.userFullname)
                    putExtra("locationDesc", product.locationDesc)
                    putExtra("theme", product.theme)
                    putExtra("userCompany", product.userCompany)
                    putExtra("category", product.category)
                    putExtra("longitude", product.longitude)
                    putExtra("height", product.height)
                    putExtra("description", product.description)
                    putExtra("rating", product.rating)
                }
                context.startActivity(intent)
            }
        }
    }
}
