package com.capstoneproject.data.dummy

import com.capstoneproject.R
import com.capstoneproject.data.model.RecommendationLocation

object RecommendationLocationData {

    private val locationNames = arrayOf(
        "Jakarta",
        "Bandung",
        "Yogyakarta",
        "Surabaya"
    )

    private val locationDescs = arrayOf(
        "lorem",
        "lorem",
        "lorem",
        "lorem",
    )

    private val locationImages = arrayOf(
        R.drawable.jakarta,
        R.drawable.bandung,
        R.drawable.jogja,
        R.drawable.surabaya
    )

    val listData: ArrayList<RecommendationLocation>
        get() {
            val list = arrayListOf<RecommendationLocation>()
            for (position in locationImages.indices) {
                val recommendationLocation = RecommendationLocation()
                recommendationLocation.name = locationNames[position]
                recommendationLocation.desc = locationDescs[position]
                recommendationLocation.photo = locationImages[position]
                list.add(recommendationLocation)
            }
            return list
        }
}