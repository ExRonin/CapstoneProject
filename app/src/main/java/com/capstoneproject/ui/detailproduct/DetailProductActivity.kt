package com.capstoneproject.ui.detailproduct

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstoneproject.R
import com.capstoneproject.data.model.product.Product
import com.capstoneproject.databinding.ActivityDetailProductBinding
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale


class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding
    private lateinit var imageUrlList: ArrayList<String>
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        product = intent.getParcelableExtra<Product>("extra_product")!!

        setProductDetailInformation()

        onAction()

    }

    private fun onAction() {
        binding.apply {
            btnBackDetailProduct.setOnClickListener {
                finish()
            }
        }
    }

    private fun setProductDetailInformation() {
        binding.apply {
            Glide.with(this@DetailProductActivity).load(product.userImage).into(ivUser)
            Glide.with(this@DetailProductActivity).load(product.imageUrl).into(sliderImage)
            binding.type.text = product.type
            binding.typetext.text = product.type
            binding.rating.text = product.rating.toString()
            binding.tvProductName.text = product.name
            binding.tvLocationProduct.text = product.locationDesc
            binding.tvProductDesc.text = product.description
            val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            formatter.currency = Currency.getInstance("IDR")
            binding.price.text = formatter.format(product.price)
            binding.tvNameAndCompany.text = product.userFullname
            binding.tvUserPosition.text = product.userPosition
            binding.widthtext.text = product.width.toString()
            binding.heighttext.text = product.height.toString()
            when (product.type) {
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
        }
    }
}


