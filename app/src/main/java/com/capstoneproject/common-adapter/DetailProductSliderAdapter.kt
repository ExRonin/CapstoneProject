package com.capstoneproject.`common-adapter`

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.capstoneproject.R
import com.smarteist.autoimageslider.SliderViewAdapter

class DetailProductSliderAdapter(imageUrl: ArrayList<String>) :
    SliderViewAdapter<DetailProductSliderAdapter.SliderViewHolder>() {

    var sliderList: ArrayList<String> = imageUrl

    class SliderViewHolder(itemView: View?) : ViewHolder(itemView) {

        var imageView: ImageView = itemView!!.findViewById(R.id.myimage)
    }

    override fun getCount(): Int {
        return sliderList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        val inflate: View =
            LayoutInflater.from(parent!!.context)
                .inflate(R.layout.item_image_slider_detail_product, null)
        return SliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        if (viewHolder != null) {
            Glide.with(viewHolder.itemView).load(sliderList[position]).fitCenter()
                .into(viewHolder.imageView)
        }
    }

}