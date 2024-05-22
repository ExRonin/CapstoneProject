package com.capstoneproject.ui.splashscreen


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.R
import com.capstoneproject.ui.login.LoginActivity
import com.capstoneproject.ui.onboardscreen.OnboardingActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        GlobalScope.launch {
            delay(3000)
            withContext(Dispatchers.Main) {
                val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
                val isFirstRun = sharedPreferences.getBoolean("is_first_run", true)

                if (isFirstRun) {
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("is_first_run", false)
                    editor.apply()
                    startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
                finish()
            }
        }
    }
}
