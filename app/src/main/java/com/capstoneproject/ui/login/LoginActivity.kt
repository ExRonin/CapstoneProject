package com.capstoneproject.ui.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.capstoneproject.R
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.login.LoginRequest
import com.capstoneproject.data.model.login.LoginResponse
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import com.capstoneproject.databinding.ActivityLoginBinding
import com.capstoneproject.ui.main.MainActivity
import com.capstoneproject.ui.register.RegisterActivity
import com.capstoneproject.ui.survey.ActivitySurvey
import com.capstoneproject.utils.showDialogError
import com.capstoneproject.utils.showDialogLoading
import com.capstoneproject.utils.showDialogSuccess
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var dialogLoading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[LoginViewModel::class.java]
        dialogLoading = showDialogLoading(this)

        onAction()
    }

    private fun onAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                if (checkValidation(email, password)) {
                    login(email, password)
                }
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        loginViewModel.loginUser(loginRequest)
        loginViewModel.loginResponse.observe(this) { response ->
            when (response) {
                is Resource.Error -> {
                    dialogLoading.dismiss()
                    showDialogError(this@LoginActivity, response.message.toString())
                }

                is Resource.Loading -> {
                    dialogLoading.show()
                }

                is Resource.Success -> {
                    dialogLoading.dismiss()
                    successAction(response.data)
                }
            }
        }
    }

    private fun successAction(data: LoginResponse?) {
        if (data != null) {
            loginViewModel.saveDataToken(data.data.id, data.token, data.refreshToken)
            val dialogSuccess = showDialogSuccess(
                this,
                getString(R.string.selamat_anda_berhasil_masuk)
            )
            dialogSuccess.show()

            Handler(Looper.getMainLooper())
                .postDelayed({
                    dialogSuccess.dismiss()
                    if (data.data.isFillSurvey) {
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        startActivity(Intent(this, ActivitySurvey::class.java))
                    }
                    finishAffinity()
                }, 1500)
        }
    }


    private fun checkValidation(email: String, pass: String): Boolean {
        binding.apply {
            when {
                email.isEmpty() -> {
                    etEmail.error = getString(R.string.silahkan_isi_email_terlebih_dahulu)
                    etEmail.requestFocus()
                }

                pass.isEmpty() -> {
                    etPassword.error = getString(R.string.silahan_isi_kata_sandi_terlebih_dahulu)
                    etPassword.requestFocus()
                }

                else -> return true
            }
        }
        return false
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

