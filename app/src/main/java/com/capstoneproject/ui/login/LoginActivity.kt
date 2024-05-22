package com.capstoneproject.ui.login


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.ui.main.MainActivity
import com.capstoneproject.databinding.ActivityLoginBinding
import com.capstoneproject.ui.register.RegisterActivity


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
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }
}
