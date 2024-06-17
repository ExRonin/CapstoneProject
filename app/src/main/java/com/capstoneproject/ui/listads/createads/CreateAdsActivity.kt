package com.capstoneproject.ui.listads.createads

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.R
import com.capstoneproject.data.Resource
import com.capstoneproject.databinding.ActivityCreateAdsBinding
import com.capstoneproject.utils.hideSoftKeyboard
import com.capstoneproject.utils.reduceFileImage
import com.capstoneproject.utils.showDialogError
import com.capstoneproject.utils.showDialogLoading
import com.capstoneproject.utils.showDialogSuccess
import com.capstoneproject.utils.uriToFile

class CreateAdsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAdsBinding
    private lateinit var createAdsViewModel: CreateAdsViewModel
    private var categoryAds = ""
    private lateinit var dialogLoading: AlertDialog
    private var currentImageUri: Uri? = null
    private lateinit var idUser: String
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createAdsViewModel = ViewModelProvider(this)[CreateAdsViewModel::class.java]
        dialogLoading = showDialogLoading(this)
        getInformationFromIntent()
        onAction()

    }

    private fun getInformationFromIntent() {
        idUser = intent.getStringExtra("user_id") ?: ""
        token = intent.getStringExtra("token") ?: ""
    }

    private fun checkValidation(title: String): Boolean {
        binding.apply {
            when {
                title.isEmpty() -> {
                    etTitle.error = getString(R.string.silahkan_masukkan_judul_iklan)
                    etTitle.requestFocus()
                }

                else -> return true
            }
        }
        return false
    }

    private fun uploadAds(title: String) {
        if (currentImageUri != null) {
            val imageFile = uriToFile(currentImageUri!!, this).reduceFileImage()
            createAdsViewModel.createAds(
                token = token,
                userId = idUser,
                name = title,
                category = categoryAds,
                image = imageFile
            )
            createAdsViewModel.createAdsResponse.observe(this@CreateAdsActivity) { respones ->
                when (respones) {
                    is Resource.Error -> {
                        dialogLoading.dismiss()
                        showDialogError(this, respones.message.toString())
                    }
                    is Resource.Loading -> {
                        dialogLoading.show()
                    }
                    is Resource.Success -> {
                        dialogLoading.dismiss()
                        val dialogSuccess = showDialogSuccess(
                            this,
                            getString(R.string.selamat_anda_berhasil_membuat_iklan)
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
        } else {
            Toast.makeText(
                this,
                getString(R.string.tidak_ada_gambar_yang_dipilih),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onAction() {
        binding.apply {
            btnCloseAddNewStory.setOnClickListener {
                finish()
            }

            radioDataChange()

            btnUpload.setOnClickListener {
                var title = etTitle.text.toString().trim()

                if (!rbFood.isChecked && !rbEvent.isChecked && !rbCommercial.isChecked && !rbSocial.isChecked) {
                    rbEvent.setError(getString(R.string.pilih_kategori_iklan))
                    rbFood.setError(getString(R.string.pilih_kategori_iklan))
                    rbCommercial.setError(getString(R.string.pilih_kategori_iklan))
                    rbSocial.setError(getString(R.string.pilih_kategori_iklan))
                } else {
                    if (checkValidation(title)) {
                        hideSoftKeyboard(this@CreateAdsActivity, binding.root)
                        uploadAds(title)
                    }
                }
            }

            ivAds.setOnClickListener {
                startOpenGallery()
            }


        }
    }

    private fun startOpenGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(
                this,
                getString(R.string.tidak_ada_gambar_yang_dipilih), Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivAds.setImageURI(it)
        }
    }

    private fun radioDataChange() {
        binding.radioGroup1.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_event -> {
                    categoryAds = "Event"
                    removeErrorNotif()
                }

                R.id.rb_food -> {
                    categoryAds = "Makanan dan Minuman"
                    removeErrorNotif()
                }

                R.id.rb_commercial -> {
                    categoryAds = "Produk Komersial"
                    removeErrorNotif()
                }

                R.id.rb_social -> {
                    categoryAds = "Social dan Komunitas"
                    removeErrorNotif()
                }
            }
        }
    }

    private fun removeErrorNotif() {
        binding.rbFood.setError(null)
        binding.rbEvent.setError(null)
        binding.rbSocial.setError(null)
        binding.rbCommercial.setError(null)
    }
}