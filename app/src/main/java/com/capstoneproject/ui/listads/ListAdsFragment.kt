package com.capstoneproject.ui.listads

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
import com.capstoneproject.data.model.advertisingcontent.AdvertisingContent
import com.capstoneproject.data.model.advertisingcontent.AdvertisingContentsResponse
import com.capstoneproject.databinding.FragmentListAdsBinding
import com.capstoneproject.ui.listads.adapter.ListAdsAdapter
import com.capstoneproject.utils.gone
import com.capstoneproject.utils.visible

class ListAdsFragment : Fragment() {

    private var _binding: FragmentListAdsBinding? = null
    private val binding get() = _binding!!
    private lateinit var listAdsViewModel: ListAdsViewModel
    private lateinit var listAdsAdapter: ListAdsAdapter
    private var idUser = ""
    private var token = ""
    private var refreshToken = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListAdsBinding.inflate(inflater, container, false)

        listAdsViewModel = ViewModelProvider(this)[ListAdsViewModel::class.java]
        listAdsAdapter = ListAdsAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getIdAndToken()
        onAction()
    }

    private fun onAction() {
        binding.apply {
            etSearchAds.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val dataSearch = s.toString().trim()

                    if (dataSearch.isEmpty()) {
                        listAdsAdapter.filter.filter("")
                        tvErrorMessage.text = requireContext().getString(R.string.default_text)
                        containerError.gone()
                        rvListAds.visible()
                    } else {
                        listAdsAdapter.filter.filter(dataSearch)
                        if (listAdsAdapter.itemCount == 0) {
                            errorAction(getString(R.string.order_tidak_ditemukan))
                        } else {
                            tvErrorMessage.text = requireContext().getString(R.string.default_text)
                            containerError.gone()
                            rvListAds.visible()
                        }
                    }
                }
            })
        }
    }

    private fun getIdAndToken() {
        listAdsViewModel.getDataToken()
        listAdsViewModel.idUser.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                idUser = response
            }
        })
        listAdsViewModel.token.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                token = response
            }
        })
        listAdsViewModel.refreshToken.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                refreshToken = response
                observeDataAdvertisingContent()
            }
        })
    }

    private fun observeDataAdvertisingContent() {
        if (token != "" && idUser != "") {
            listAdsViewModel.getAdvertisingContentsByUserId(id = idUser, token = token)
            listAdsViewModel.advertisingContentsByUserIdResponse.observe(viewLifecycleOwner, Observer { response ->
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

    private fun successAction(data: AdvertisingContentsResponse?) {
        if (data != null) {
            binding.apply {
                if (data.data?.isEmpty() == true) {
                    errorAction(getString(R.string.order_tidak_ditemukan))
                } else {
                    listAdsAdapter.setOrdersList(data.data as List<AdvertisingContent>)
                    rvListAds.apply {
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        adapter = listAdsAdapter
                    }
                    tvErrorMessage.text = requireContext().getString(R.string.default_text)
                    containerError.gone()
                    viewShimmer.stopShimmerAnimation()
                    viewShimmer.gone()
                    rvListAds.visible()
                    Log.d(TAG, "datanya : ${data.data}")
                }
            }
        }
    }

    private fun loadingAction() {
        binding.apply {
            rvListAds.gone()
            tvErrorMessage.text = requireContext().getString(R.string.default_text)
            containerError.gone()
            viewShimmer.startShimmerAnimation()
            viewShimmer.visible()
        }
    }

    private fun errorAction(message: String?) {
        binding.apply {
            rvListAds.gone()
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
        const val TAG = "ListAdsFragment"
    }

}