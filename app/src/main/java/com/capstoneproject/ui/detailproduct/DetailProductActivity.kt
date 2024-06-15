package com.capstoneproject.ui.detailproduct

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstoneproject.R
import com.capstoneproject.databinding.ActivityDetailProductBinding
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale


class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding
    private lateinit var imageUrlList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val id = intent.getStringExtra("id")
        val endBooked = intent.getStringExtra("endBooked")
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val isBooked = intent.getBooleanExtra("isBooked", false)
        val type = intent.getStringExtra("type")
        val userId = intent.getStringExtra("userId")
        val startBooked = intent.getStringExtra("startBooked")
        val userImage = intent.getStringExtra("userImage")
        val price = intent.getIntExtra("price", 0)
        val imageUrl = intent.getStringExtra("imageUrl")
        val userPosition = intent.getStringExtra("userPosition")
        val name = intent.getStringExtra("name")
        val width = intent.getIntExtra("width", 0)
        val userFullname = intent.getStringExtra("userFullname")
        val locationDesc = intent.getStringExtra("locationDesc")
        val theme = intent.getStringExtra("theme")
        val userCompany = intent.getStringExtra("userCompany")
        val category = intent.getStringExtra("category")
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val height = intent.getIntExtra("height", 0)
        val description = intent.getStringExtra("description")
        val rating = intent.getDoubleExtra("rating", 0.0)


        binding.apply {
            Glide.with(this@DetailProductActivity).load(userImage).into(ivUser)
            Glide.with(this@DetailProductActivity).load(imageUrl).into(sliderImage)
            binding.type.text = type
            binding.typetext.text = type
            binding.rating.text = rating.toString()
            binding.tvProductName.text = name
            binding.tvLocationProduct.text = locationDesc
            binding.tvProductDesc.text = description
            val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            formatter.currency = Currency.getInstance("IDR")
            binding.price.text = formatter.format(price)
            binding.tvNameAndCompany.text = userFullname
            binding.tvUserPosition.text = userPosition
            binding.widthtext.text = width.toString()
            binding.heighttext.text = height.toString()
            when (type) {
                "Videotron" -> {
                    Glide.with(this@DetailProductActivity).load(R.drawable.typevid).into(typeimage)
                    Glide.with(this@DetailProductActivity).load(R.drawable.typevideotron)
                        .into(typeimageb)
                }

                "Baliho" -> {
                    Glide.with(this@DetailProductActivity).load(R.drawable.typebal).into(typeimage)
                    Glide.with(this@DetailProductActivity).load(R.drawable.typebaliho)
                        .into(typeimageb)
                }

                "Billboard" -> {
                    Glide.with(this@DetailProductActivity).load(R.drawable.typebil).into(typeimage)
                    Glide.with(this@DetailProductActivity).load(R.drawable.billboard)
                        .into(typeimageb)
                }

                "Bando Jalan" -> {
                    Glide.with(this@DetailProductActivity).load(R.drawable.typestret).into(typeimage)
                    Glide.with(this@DetailProductActivity).load(R.drawable.street)
                        .into(typeimageb)
                }

                else -> {
                    Glide.with(this@DetailProductActivity).load(R.drawable.typevid).into(typeimage)
                    Glide.with(this@DetailProductActivity).load(R.drawable.typevideotron)
                }
            }

            binding.btnBackDetailProduct.setOnClickListener {
                @Suppress("DEPRECATION")
                onBackPressed()


            }

        }
    }
}


