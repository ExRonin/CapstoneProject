package com.capstoneproject.ui.bookingprocess.bookingdetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstoneproject.R
import com.capstoneproject.databinding.FragmentBookingDetailBinding
import com.capstoneproject.ui.bookingprocess.bookingdetail.edit_profile.EditProfileOrdererActivity
import com.capstoneproject.ui.bookingprocess.bookingpayment.BookingPaymentFragment


class BookingDetailFragment : Fragment() {

    private var _binding: FragmentBookingDetailBinding? = null
    private val binding get() = _binding!!
    private var userFullName = "Paula Sugiarto"
    private var userEmail = "paulasugiarto@gmail.com"
    private var userPhone = "62895329016827"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
//            btn.setOnClickListener {
//                val fragmentManager = parentFragmentManager
//                val fragmentTransaction = fragmentManager.beginTransaction()
//                fragmentTransaction.replace(R.id.fragment_container, BookingPaymentFragment())
//                fragmentTransaction.commit()
//            }
        }

        setInformationBooking()
        onAction()
    }

    private fun setInformationBooking() {
        binding.apply {
            tvFullnameUser.text = userFullName
            tvEmailUser.text = userEmail
            tvPhoneUser.text = userPhone
        }
    }

    private fun onAction() {
        binding.apply {
            btnEditProfile.setOnClickListener {
                val intent = Intent(requireContext(), EditProfileOrdererActivity::class.java)
                intent.putExtra("userFullName", userFullName)
                intent.putExtra("userEmail", userEmail)
                intent.putExtra("userPhone", userPhone)
                startActivityForResult(intent, REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            userFullName = data?.getStringExtra("userFullNameReturn").toString()
            userPhone = data?.getStringExtra("userPhoneReturn").toString()
            userEmail = data?.getStringExtra("userEmailReturn").toString()
            binding.apply {
                tvFullnameUser.text = userFullName
                tvPhoneUser.text = userPhone
                tvEmailUser.text = userEmail
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_CODE = 1
    }

}