package com.capstoneproject.ui.register


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.R
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.register.RegisterRequest
import com.capstoneproject.databinding.ActivityRegisterBinding
import com.capstoneproject.ui.login.LoginActivity
import com.capstoneproject.ui.main.MainActivity
import com.capstoneproject.ui.survey.ActivitySurvey
import com.capstoneproject.utils.hideSoftKeyboard
import com.capstoneproject.utils.showDialogError
import com.capstoneproject.utils.showDialogLoading
import com.capstoneproject.utils.showDialogSuccess

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var dialogLoading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        dialogLoading = showDialogLoading(this)

        onAction()

    }

    private fun onAction() {
        binding.apply {
            btnRegister.setOnClickListener {
                val username = etUsername.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val fullname = etFullname.text.toString()
                val phone = etPhone.text.toString().trim()
                val password = etPassword.text.toString().trim()
                val confirmPassword = etPasswordConfirm.text.toString().trim()
                if (checkValidation(username, email, fullname, phone, password, confirmPassword)) {
                    hideSoftKeyboard(this@RegisterActivity, binding.root)
                    register(username, email, fullname, phone, password)
                }
            }

            tvLogin.setOnClickListener {
                finish()
            }

            btnBackRegister.setOnClickListener {
                finish()
            }
        }
    }

    private fun register(
        username: String,
        email: String,
        fullname: String,
        phone: String,
        password: String
    ) {
        val registerRequest = RegisterRequest(
            username = username,
            email = email,
            fullname = fullname,
            phone = phone,
            password = password,
            role = "customer"
        )
        registerViewModel.registerUser(registerRequest)
        registerViewModel.registerResponse.observe(this) { response ->
            when (response) {
                is Resource.Error -> {
                    dialogLoading.dismiss()
                    showDialogError(this@RegisterActivity, response.message.toString())
                }
                is Resource.Loading -> {
                    dialogLoading.show()
                }
                is Resource.Success -> {
                    dialogLoading.dismiss()
                    val dialogSuccess = showDialogSuccess(
                        this,
                        getString(R.string.selamat_anda_berhasil_mendaftarkan_akun)
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

    private fun checkValidation(
        username: String,
        email: String,
        fullname: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        binding.apply {
            when {
                username.isEmpty() -> {
                    etUsername.error = getString(R.string.silahkan_isi_username_terlebih_dahulu)
                    etUsername.requestFocus()
                }

                email.isEmpty() -> {
                    etEmail.error = getString(R.string.silahkan_isi_email_terlebih_dahulu)
                    etEmail.requestFocus()
                }

                fullname.isEmpty() -> {
                    etFullname.error = getString(R.string.silahkan_isi_nama_lengkap_terlebih_dahulu)
                    etFullname.requestFocus()
                }

                phone.isEmpty() -> {
                    etPhone.error = getString(R.string.silahkan_isi_nomor_telepon_terlebih_dahulu)
                    etPhone.requestFocus()
                }

                password.length < 6 -> {
                    etPassword.error =
                        getString(R.string.silahkan_isi_kata_sandi_minimal_6_karakter)
                    etPassword.requestFocus()
                }

                confirmPassword != password -> {
                    etPasswordConfirm.error =
                        getString(R.string.konfirmasi_kata_sandi_tidak_sesuai_dengan_kata_sandi)
                    etPasswordConfirm.requestFocus()
                }

                else -> return true
            }
        }
        return false
    }

    companion object {
        const val TAG = "RegisterActivity"
    }
}

