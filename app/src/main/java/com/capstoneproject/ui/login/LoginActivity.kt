package com.capstoneproject.ui.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.capstoneproject.data.source.remote.network.LoginRequest
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import com.capstoneproject.databinding.ActivityLoginBinding
import com.capstoneproject.ui.register.RegisterActivity
import com.capstoneproject.ui.survey.ActivitySurvey
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onAction()
    }

    private fun onAction() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    login(email, password)
                } else {
                    Toast.makeText(this@LoginActivity, "Email and Password must not be empty", Toast.LENGTH_SHORT).show()
                }
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun login(email: String, password: String) {
        val request = LoginRequest(email, password)
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.login(request)
                if (response.status) {
                    val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit()
                        .putString("token", response.token)
                        .putString("refreshToken", response.refreshToken)
                        .apply()
                    startActivity(Intent(this@LoginActivity, ActivitySurvey::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, response.message, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

