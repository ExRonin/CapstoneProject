package com.capstoneproject.ui.bookingprocess

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstoneproject.R
import com.capstoneproject.data.model.product.Product
import com.capstoneproject.databinding.ActivityBookingProcessBinding
import com.capstoneproject.ui.bookingprocess.bookingdetail.BookingDetailFragment

class BookingProcessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingProcessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingProcessBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val product: Product = intent.getParcelableExtra("extra_product")!!
        setFragment(product)
    }

    private fun setFragment(product: Product) {
        val fragment = BookingDetailFragment()
        val bundle = Bundle()
        bundle.putParcelable("extra_product", product)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}