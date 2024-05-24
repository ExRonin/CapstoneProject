package com.capstoneproject.ui.bookingprocess

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstoneproject.R
import com.capstoneproject.databinding.ActivityBookingProcessBinding
import com.capstoneproject.ui.bookingprocess.bookingdetail.BookingDetailFragment

class BookingProcessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingProcessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingProcessBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        setFragment()
    }

    private fun setFragment() {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BookingDetailFragment())
                .commit()

    }
}