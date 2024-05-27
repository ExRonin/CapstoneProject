package com.capstoneproject.ui.register


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.databinding.ActivityRegisterBinding
import com.capstoneproject.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onAction()

    }

    private fun onAction() {
        binding.apply {
            btnRegister.setOnClickListener {
                finish()
            }

            tvLogin.setOnClickListener {
                finish()
            }

            btnBackRegister.setOnClickListener {
                finish()
            }
        }
    }
}
