package com.capstoneproject.ui.listshowads

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.R
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.order.Order
import com.capstoneproject.data.model.order.OrderResponse
import com.capstoneproject.databinding.FragmentListBookingBinding
import com.capstoneproject.databinding.FragmentListShowAdsBinding
import com.capstoneproject.ui.listbooking.ListBookingFragment
import com.capstoneproject.ui.listshowads.adapter.ListShowAdsAdapter
import com.capstoneproject.utils.gone
import com.capstoneproject.utils.visible

class ListShowAdsFragment : Fragment() {

    private var _binding: FragmentListShowAdsBinding? = null
    private val binding get() = _binding!!
    private lateinit var listShowAdsViewModel: ListShowAdsViewModel
    private lateinit var listShowAdsAdapter: ListShowAdsAdapter
    private var idUser = ""
    private var token = ""
    private var refreshToken = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListShowAdsBinding.inflate(inflater, container, false)

        listShowAdsViewModel = ViewModelProvider(this)[ListShowAdsViewModel::class.java]
        listShowAdsAdapter = ListShowAdsAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getIdAndToken()
        onAction()
    }

    private fun onAction() {
        binding.apply {
            etSearchShowAds.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val dataSearch = s.toString().trim()

                    if (dataSearch.isEmpty()) {
                        listShowAdsAdapter.filter.filter("")
                        tvErrorMessage.text = requireContext().getString(R.string.default_text)
                        containerError.gone()
                        rvListShowAds.visible()
                    } else {
                        listShowAdsAdapter.filter.filter(dataSearch)
                        if (listShowAdsAdapter.itemCount == 0) {
                            errorAction(getString(R.string.order_tidak_ditemukan))
                        } else {
                            tvErrorMessage.text = requireContext().getString(R.string.default_text)
                            containerError.gone()
                            rvListShowAds.visible()
                        }
                    }
                }
            })
        }
    }

    private fun getIdAndToken() {
        listShowAdsViewModel.getDataToken()
        listShowAdsViewModel.idUser.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                idUser = response
            }
        })
        listShowAdsViewModel.token.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                token = response
            }
        })
        listShowAdsViewModel.refreshToken.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                refreshToken = response
                observeDataOrder()
            }
        })
    }

    private fun observeDataOrder() {
        if (token != "" && idUser != "") {
            listShowAdsViewModel.getOrdersShowAdvertisementByUserId(id = idUser, token = token)
            listShowAdsViewModel.ordersShowAdvertisementByUserIdResponse.observe(
                viewLifecycleOwner,
                Observer { response ->
                    when (response) {
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
            binding.apply {
                if (data.data?.isEmpty() == true) {
                    errorAction(getString(R.string.order_tidak_ditemukan))
                } else {
                    listShowAdsAdapter.setOrdersList(data.data as List<Order>)
                    rvListShowAds.apply {
                        layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        adapter = listShowAdsAdapter
                    }
                    tvErrorMessage.text = requireContext().getString(R.string.default_text)
                    containerError.gone()
                    viewShimmer.stopShimmerAnimation()
                    viewShimmer.gone()
                    rvListShowAds.visible()
                    Log.d(ListBookingFragment.TAG, "datanya : ${data.data}")
                }
            }
        }
    }

    private fun loadingAction() {
        binding.apply {
            rvListShowAds.gone()
            tvErrorMessage.text = requireContext().getString(R.string.default_text)
            containerError.gone()
            viewShimmer.startShimmerAnimation()
            viewShimmer.visible()
        }
    }

    private fun errorAction(message: String?) {
        binding.apply {
            rvListShowAds.gone()
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

}