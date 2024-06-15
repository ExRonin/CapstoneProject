package com.capstoneproject.ui.bookingprocess.bookingreceipt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.`common-adapter`.TutorialPaymentAdapter
import com.capstoneproject.data.dummy.TutorialPaymentData
import com.capstoneproject.data.model.TutorialPayment
import com.capstoneproject.databinding.FragmentBookingReceiptBinding

class BookingReceiptFragment : Fragment() {

    private var _binding: FragmentBookingReceiptBinding? = null
    private val binding get() = _binding!!
    private lateinit var tutorialPaymentAdapter: TutorialPaymentAdapter
    private var list: ArrayList<TutorialPayment> = arrayListOf()

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

        setTutorialPaymentRV()
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