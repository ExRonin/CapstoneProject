package com.capstoneproject.ui.bookingprocess.bookingreceipt

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstoneproject.R
import com.capstoneproject.`common-adapter`.TutorialPaymentAdapter
import com.capstoneproject.data.dummy.TutorialPaymentData
import com.capstoneproject.data.model.TutorialPayment
import com.capstoneproject.data.model.order.Order
import com.capstoneproject.databinding.FragmentBookingReceiptBinding
import com.capstoneproject.ui.main.MainActivity
import com.capstoneproject.utils.formatCurrency

class BookingReceiptFragment : Fragment() {

    private var _binding: FragmentBookingReceiptBinding? = null
    private val binding get() = _binding!!
    private lateinit var tutorialPaymentAdapter: TutorialPaymentAdapter
    private var list: ArrayList<TutorialPayment> = arrayListOf()
    private lateinit var orderData: Order

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingReceiptBinding.inflate(inflater, container, false)

        tutorialPaymentAdapter = TutorialPaymentAdapter()

        list.addAll(TutorialPaymentData.listData)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderData = arguments?.getParcelable("order_response")!!
        setOrderResponse()
        setTutorialPaymentRV()
        onAction()
    }

    private fun onAction() {
        binding.apply {
            btnCloseReceipt.setOnClickListener {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                activity?.finishAffinity()
            }

            btnClose.setOnClickListener {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                activity?.finishAffinity()
            }
        }
    }

    private fun setOrderResponse() {
        orderData.let {
            binding.apply {
                tvProductBookingDate.text = "${orderData.startBooked} s/d ${orderData.endBooked}"
                tvProductName.text = orderData.productName
                tvBankName.text = orderData.paymentMethod
                tvVirtualNumber.text = "3302 8272 2019"
                tvTotalPayment.text = formatCurrency(orderData.totalPayment?.toInt() ?: 0)
                if (orderData.paymentMethod == "Bca") {
                    Glide.with(requireContext())
                        .load(R.drawable.icon_bca)
                        .into(ivBank)
                }
                if (orderData.paymentMethod == "Bri") {
                    Glide.with(requireContext())
                        .load(R.drawable.icon_bri)
                        .into(ivBank)
                }
                if (orderData.paymentMethod == "Mandiri") {
                    Glide.with(requireContext())
                        .load(R.drawable.icon_mandiri)
                        .into(ivBank)
                }
                Glide.with(requireContext())
                    .load(orderData.imageProduct)
                    .into(ivProduct)
            }
        }
    }

    private fun setTutorialPaymentRV() {
        tutorialPaymentAdapter.setListTutorial(list)
        binding.rvListTutorial.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = tutorialPaymentAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}