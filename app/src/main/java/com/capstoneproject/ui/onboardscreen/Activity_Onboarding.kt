package com.capstoneproject.ui.onboardscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.databinding.ActivityOnboardingBinding
import com.capstoneproject.ui.login.LoginActivity
import com.capstoneproject.ui.onboardscreen.adapter.OnboardingAdapter
import com.capstoneproject.ui.register.RegisterActivity


class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreateAccount.setOnClickListener {
            val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("is_first_run", false)
            editor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.viewPager.adapter = OnboardingAdapter(supportFragmentManager, this)
        binding.viewPager.offscreenPageLimit = 1
    }
}