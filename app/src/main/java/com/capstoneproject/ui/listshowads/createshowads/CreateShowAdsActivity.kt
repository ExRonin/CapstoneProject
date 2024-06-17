package com.capstoneproject.ui.listshowads.createshowads

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstoneproject.R
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.advertisingcontent.AdvertisingContent
import com.capstoneproject.data.model.order.CreateShowAdsRequest
import com.capstoneproject.data.model.order.Order
import com.capstoneproject.databinding.ActivityCreateShowAdsBinding
import com.capstoneproject.utils.gone
import com.capstoneproject.utils.showDialogError
import com.capstoneproject.utils.showDialogLoading
import com.capstoneproject.utils.showDialogSuccess
import com.capstoneproject.utils.visible

class CreateShowAdsActivity : AppCompatActivity() {
    private val REQUEST_CODE_ADS = 1
    private val REQUEST_CODE_MEDIA = 2
    private lateinit var binding: ActivityCreateShowAdsBinding
    private lateinit var createShowAdsViewModel: CreateShowAdsViewModel
    private lateinit var dialogLoading: AlertDialog
    private lateinit var idUser: String
    private lateinit var token: String
    private var product: Order? = null
    private var ads: AdvertisingContent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateShowAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createShowAdsViewModel = ViewModelProvider(this)[CreateShowAdsViewModel::class.java]
        dialogLoading = showDialogLoading(this)

        getInformationFromIntent()
        onAction()
    }

    private fun onAction() {
        binding.apply {
            ivChooseMediaAds.setOnClickListener {
                val intent =
                    Intent(this@CreateShowAdsActivity, ListMediaAdsActivity::class.java).apply {
                        putExtra("user_id", idUser)
                        putExtra("token", token)
                    }
                startActivityForResult(intent, REQUEST_CODE_MEDIA)
            }

            ibEditMediaAds.setOnClickListener {
                val intent =
                    Intent(this@CreateShowAdsActivity, ListMediaAdsActivity::class.java).apply {
                        putExtra("user_id", idUser)
                        putExtra("token", token)
                    }
                startActivityForResult(intent, REQUEST_CODE_MEDIA)
            }

            btnChooseAds.setOnClickListener {
                val intent = Intent(this@CreateShowAdsActivity, ListAdsActivity::class.java).apply {
                    putExtra("user_id", idUser)
                    putExtra("token", token)
                }
                startActivityForResult(intent, REQUEST_CODE_ADS)
            }

            ibEditAds.setOnClickListener {
                val intent = Intent(this@CreateShowAdsActivity, ListAdsActivity::class.java).apply {
                    putExtra("user_id", idUser)
                    putExtra("token", token)
                }
                startActivityForResult(intent, REQUEST_CODE_ADS)
            }

            btnCloseAddNewShowAds.setOnClickListener {
                finish()
            }

            btnShowAds.setOnClickListener {
                createShowAds()
            }
        }
    }

    private fun createShowAds() {
        if (product == null || ads == null) {
            Toast.makeText(
                this,
                "Silahkan pilih media iklan dan konten iklan terlebih dahulu",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val createShowAdsRequest =
                CreateShowAdsRequest(advertisingContentId = ads!!.id, orderId = product!!.id)
            createShowAdsViewModel.createShowAds(token, createShowAdsRequest)
            createShowAdsViewModel.createShowAdsResponse.observe(this) { response ->
                when (response) {
                    is Resource.Error -> {
                        dialogLoading.dismiss()
                        showDialogError(this, response.message.toString())
                    }

                    is Resource.Loading -> {
                        dialogLoading.show()
                    }

                    is Resource.Success -> {
                        dialogLoading.dismiss()
                        val dialogSuccess = showDialogSuccess(
                            this,
                            getString(R.string.selamat_anda_berhasil_membuat_penayangan_iklan)
                        )
                        dialogSuccess.show()

                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                dialogSuccess.dismiss()
                                finish()
                            }, 1500)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_ADS -> {
                    val selectedAd: AdvertisingContent? = data?.getParcelableExtra("selected_ad")
                    ads = selectedAd
                    setInformationAds()
                }

                REQUEST_CODE_MEDIA -> {
                    val selectedMedia: Order? = data?.getParcelableExtra("selected_media")
                    product = selectedMedia
                    setInformationMediaAds()
                }
            }
        }
    }

    private fun setInformationMediaAds() {
        product.let {
            binding.apply {
                ibEditMediaAds.visible()
                containerChooseMediaAds.gone()
                containerShowChooseMediaAds.visible()
                Glide.with(this@CreateShowAdsActivity)
                    .load(product?.imageProduct)
                    .into(ivProductMediaAds)
                tvProductNameMediaAds.text = product?.productName ?: ""
                tvProductBookingDate.text = "${product?.startBooked} s/d ${product?.endBooked}"
                tvProductAddress.text = product?.locationProduct ?: ""
            }
        }

    }

    private fun setInformationAds() {
        ads.let {
            binding.apply {
                ibEditAds.visible()
                btnChooseAds.gone()
                containerChooseAds.visible()
                tvChooseAdsTitle.text = ads?.name ?: ""
                tvChooseAdsCategory.text = ads?.category
                Glide.with(this@CreateShowAdsActivity)
                    .load(ads?.imageUrl)
                    .into(ivChooseAds)
            }
        }
    }

    private fun getInformationFromIntent() {
        idUser = intent.getStringExtra("user_id") ?: ""
        token = intent.getStringExtra("token") ?: ""
    }
}