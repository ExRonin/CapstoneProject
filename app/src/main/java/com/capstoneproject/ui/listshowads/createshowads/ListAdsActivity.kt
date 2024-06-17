package com.capstoneproject.ui.listshowads.createshowads

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstoneproject.R
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.advertisingcontent.AdvertisingContent
import com.capstoneproject.data.model.advertisingcontent.AdvertisingContentsResponse
import com.capstoneproject.databinding.ActivityListAdsBinding
import com.capstoneproject.ui.listads.ListAdsFragment
import com.capstoneproject.ui.listads.ListAdsViewModel
import com.capstoneproject.ui.listads.adapter.ListAdsAdapter
import com.capstoneproject.utils.gone
import com.capstoneproject.utils.visible

class ListAdsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListAdsBinding
    private lateinit var listAdsViewModel: ListAdsViewModel
    private lateinit var listAdsAdapter: ListAdsAdapter
    private lateinit var idUser: String
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listAdsViewModel = ViewModelProvider(this)[ListAdsViewModel::class.java]
        listAdsAdapter = ListAdsAdapter()

        getInformationFromIntent()
        observeDataAds()
        onAction()
    }

    private fun onAction() {
        binding.apply {
            etSearchAds.addTextChangedListener(object : TextWatcher {
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
                        listAdsAdapter.filter.filter("")
                        tvErrorMessage.text = getString(R.string.default_text)
                        containerError.gone()
                        rvListAds.visible()
                    } else {
                        listAdsAdapter.filter.filter(dataSearch)
                        if (listAdsAdapter.itemCount == 0) {
                            errorAction(getString(R.string.order_tidak_ditemukan))
                        } else {
                            tvErrorMessage.text = getString(R.string.default_text)
                            containerError.gone()
                            rvListAds.visible()
                        }
                    }
                }
            })

            btnBack.setOnClickListener {
                finish()
            }

            listAdsAdapter.setOnItemClickCallback(object : ListAdsAdapter.OnItemClickCallback {
                override fun onItemClicked(advertisingContent: AdvertisingContent) {
                    showAlertDialog(advertisingContent)
                }
            })
        }
    }

    private fun showAlertDialog(advertisingContent: AdvertisingContent) {
        AlertDialog.Builder(this)
            .setTitle("Pilih ${advertisingContent.name} sebagai konten iklan?")
            .setPositiveButton("Yes") { dialog, _ ->
                val resultIntent = Intent().apply {
                    putExtra("selected_ad", advertisingContent)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun observeDataAds() {
        listAdsViewModel.getAdvertisingContentsByUserId(id = idUser, token = token)
        listAdsViewModel.advertisingContentsByUserIdResponse.observe(this) { response ->
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
                        layoutManager = LinearLayoutManager(
                            this@ListAdsActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        adapter = listAdsAdapter
                    }
                    tvErrorMessage.text = getString(R.string.default_text)
                    containerError.gone()
                    viewShimmer.stopShimmerAnimation()
                    viewShimmer.gone()
                    rvListAds.visible()
                    Log.d(ListAdsFragment.TAG, "datanya : ${data.data}")
                }
            }
        }
    }

    private fun loadingAction() {
        binding.apply {
            rvListAds.gone()
            tvErrorMessage.text = getString(R.string.default_text)
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

    private fun getInformationFromIntent() {
        idUser = intent.getStringExtra("user_id") ?: ""
        token = intent.getStringExtra("token") ?: ""
    }

}