package com.capstoneproject.ui.detailproduct


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.adapter.DetailProductSliderAdapter
import com.capstoneproject.databinding.ActivityDetailProductBinding
import com.smarteist.autoimageslider.SliderView

class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding
    lateinit var imageUrl: ArrayList<String>
    lateinit var sliderAdapter: DetailProductSliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setImageDetailSlider()
    }

    private fun setImageDetailSlider() {
        imageUrl = ArrayList()
        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdsa-self-paced-thumbnail.png&w=1920&q=75") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdata-science-live-thumbnail.png&w=1920&q=75") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Ffull-stack-node-thumbnail.png&w=1920&q=75") as ArrayList<String>
        binding.apply {
            sliderAdapter = DetailProductSliderAdapter(imageUrl)
            sliderImage.autoCycleDirection =  SliderView.LAYOUT_DIRECTION_LTR
            sliderImage.setSliderAdapter(sliderAdapter)
        }

    }
}