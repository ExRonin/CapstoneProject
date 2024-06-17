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
import com.capstoneproject.data.model.order.Order
import com.capstoneproject.data.model.order.OrderResponse
import com.capstoneproject.databinding.ActivityListMediaAdsBinding
import com.capstoneproject.ui.listbooking.ListBookingFragment
import com.capstoneproject.ui.listbooking.adapter.ListBookingAdapter
import com.capstoneproject.utils.gone
import com.capstoneproject.utils.visible

class ListMediaAdsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListMediaAdsBinding
    private lateinit var listMediaAdsViewModel: ListMediaAdsViewModel
    private lateinit var listBookingAdapter: ListBookingAdapter
    private lateinit var idUser: String
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMediaAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listMediaAdsViewModel = ViewModelProvider(this)[ListMediaAdsViewModel::class.java]
        listBookingAdapter = ListBookingAdapter()

        getInformationFromIntent()
        observeDataMediaAds()
        onAction()

    }

    private fun onAction() {
        binding.apply {
            etSearchOrder.addTextChangedListener(object : TextWatcher {
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
                        listBookingAdapter.filter.filter("")
                        tvErrorMessage.text = getString(R.string.default_text)
                        containerError.gone()
                        rvListBooking.visible()
                    } else {
                        listBookingAdapter.filter.filter(dataSearch)
                        if (listBookingAdapter.itemCount == 0) {
                            errorAction(getString(R.string.order_tidak_ditemukan))
                        } else {
                            tvErrorMessage.text = getString(R.string.default_text)
                            containerError.gone()
                            rvListBooking.visible()
                        }
                    }
                }
            })

            btnBack.setOnClickListener {
                finish()
            }

            listBookingAdapter.setOnItemClickCallback(object : ListBookingAdapter.OnItemClickCallback {
                override fun onItemClicked(order: Order) {
                    showAlertDialog(order)
                }
            })
        }
    }

    private fun showAlertDialog(order: Order) {
        AlertDialog.Builder(this)
            .setTitle("Pilih ${order.productName} sebagai media iklan?")
            .setPositiveButton("Yes") { dialog, _ ->
                val resultIntent = Intent().apply {
                    putExtra("selected_media", order)
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

    private fun observeDataMediaAds() {
        listMediaAdsViewModel.getOrdersByUserId(idUser, token)
        listMediaAdsViewModel.orderByUserIdResponse.observe(this) { response ->
            when (response) {
                is Resource.Error -> errorAction(response.message)
                is Resource.Loading -> loadingAction()
                is Resource.Success -> successAction(response.data)
                else -> {}
            }
        }
    }

    private fun successAction(data: OrderResponse?) {
        if (data != null) {
            binding.apply {
                val filteredOrders = data.data?.filter { order ->
                    order?.status == "paid"
                }
                if (filteredOrders.isNullOrEmpty()) {
                    errorAction(getString(R.string.order_tidak_ditemukan))
                } else {
                    listBookingAdapter.setOrdersList(filteredOrders as List<Order>)
                    rvListBooking.apply {
                        layoutManager = LinearLayoutManager(
                            this@ListMediaAdsActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        adapter = listBookingAdapter
                    }
                    tvErrorMessage.text = getString(R.string.default_text)
                    containerError.gone()
                    viewShimmer.stopShimmerAnimation()
                    viewShimmer.gone()
                    rvListBooking.visible()
                }
            }
        }
    }

    private fun loadingAction() {
        binding.apply {
            rvListBooking.gone()
            tvErrorMessage.text = getString(R.string.default_text)
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

    private fun getInformationFromIntent() {
        idUser = intent.getStringExtra("user_id") ?: ""
        token = intent.getStringExtra("token") ?: ""
    }
}