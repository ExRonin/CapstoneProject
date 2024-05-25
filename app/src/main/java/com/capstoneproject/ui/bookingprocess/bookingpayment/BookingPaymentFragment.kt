package com.capstoneproject.ui.bookingprocess.bookingpayment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstoneproject.R
import com.capstoneproject.databinding.FragmentBookingPaymentBinding


class BookingPaymentFragment : Fragment() {

    private var _binding: FragmentBookingPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingPaymentBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}