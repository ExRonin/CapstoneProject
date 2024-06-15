package com.capstoneproject.ui.listbooking

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.R
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.order.Order
import com.capstoneproject.data.model.order.OrderResponse
import com.capstoneproject.databinding.FragmentListBookingBinding
import com.capstoneproject.ui.listbooking.adapter.ListBookingAdapter
import com.capstoneproject.utils.gone
import com.capstoneproject.utils.hideSoftKeyboard
import com.capstoneproject.utils.visible


class ListBookingFragment : Fragment() {

    private var _binding: FragmentListBookingBinding? = null
    private val binding get() = _binding!!
    private lateinit var listBookingViewModel: ListBookingViewModel
    private lateinit var listBookingAdapter: ListBookingAdapter
    private var idUser = ""
    private var token = ""
    private var refreshToken = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBookingBinding.inflate(inflater, container, false)

        listBookingViewModel = ViewModelProvider(this)[ListBookingViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getIdAndToken()
        onAction()
    }

    private fun onAction() {
        binding.apply {
            etSearchOrder.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val dataSearch = s.toString().trim()

                    if (dataSearch.isEmpty()) {
                        listBookingAdapter.filter.filter("")
                        tvErrorMessage.text = requireContext().getString(R.string.default_text)
                        containerError.gone()
                        rvListBooking.visible()
                    } else {
                        listBookingAdapter.filter.filter(dataSearch)
                        if (listBookingAdapter.itemCount == 0) {
                            errorAction(getString(R.string.order_tidak_ditemukan))
                        } else {
                            tvErrorMessage.text = requireContext().getString(R.string.default_text)
                            containerError.gone()
                            rvListBooking.visible()
                        }
                    }
                }
            })
        }
    }

    private fun getIdAndToken() {
        listBookingViewModel.getDataToken()
        listBookingViewModel.idUser.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                idUser = response
            }
        })
        listBookingViewModel.token.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                token = response
            }
        })
        listBookingViewModel.refreshToken.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                refreshToken = response
                observeDataOrder()
            }
        })
    }

    private fun observeDataOrder() {
        if (token != "" && idUser != "") {
            listBookingViewModel.getOrdersByUserId(id = idUser, token = token)
            listBookingViewModel.orderByUserIdResponse.observe(viewLifecycleOwner, Observer { response ->
                when(response) {
                    is Resource.Error -> {
                        errorAction(response.message)
                    }
                    is Resource.Loading -> {
                        loadingAction()
                    }
                    is Resource.Success -> {
                        successAction(response.data)
                    }
                }
            })
        }
    }

    private fun successAction(data: OrderResponse?) {
        if (data != null) {
            listBookingAdapter = ListBookingAdapter()
            binding.apply {
                if (data.data?.isEmpty() == true) {
                    errorAction(getString(R.string.order_tidak_ditemukan))
                } else {
                    listBookingAdapter.setOrdersList(data.data as List<Order>)
                    rvListBooking.apply {
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        adapter = listBookingAdapter
                    }
                    tvErrorMessage.text = requireContext().getString(R.string.default_text)
                    containerError.gone()
                    viewShimmer.stopShimmerAnimation()
                    viewShimmer.gone()
                    rvListBooking.visible()
                    Log.d(TAG, "datanya : ${data.data}")
                }
            }
        }
    }

    private fun loadingAction() {
        binding.apply {
            rvListBooking.gone()
            tvErrorMessage.text = requireContext().getString(R.string.default_text)
            containerError.gone()
            viewShimmer.startShimmerAnimation()
            viewShimmer.visible()
        }
    }

    private fun errorAction(message: String?) {
        binding.apply {
            rvListBooking.gone()
            viewShimmer.stopShimmerAnimation()
            viewShimmer.gone()
            tvErrorMessage.text = message
            containerError.visible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ListBookingFragment"
    }

}