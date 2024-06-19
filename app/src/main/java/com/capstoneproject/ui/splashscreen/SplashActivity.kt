package com.capstoneproject.ui.splashscreen


import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.R
import com.capstoneproject.ui.login.LoginActivity
import com.capstoneproject.ui.main.MainActivity
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
                val token = sharedPreferences.getString("token", null)
                val savedTimestamp = sharedPreferences.getLong("timestamp", 0)
                val currentTime = System.currentTimeMillis()
                Log.d("SplashActivity", "waktunya : $currentTime, $savedTimestamp, $EXPIRATION_TIME, hasil : ${currentTime - savedTimestamp}")

                if (isFirstRun) {
                    markAppAsNotFirstRun(sharedPreferences)
                    startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                } else {
                    if (token != null && isTokenValid(savedTimestamp, currentTime)) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    } else {
                        clearDataToken(sharedPreferences)
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    }
                }
                finish()
            }
        }
    }

    private fun markAppAsNotFirstRun(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_first_run", false)
        editor.apply()
    }

    private fun isTokenValid(savedTimestamp: Long, currentTime: Long): Boolean {
        return currentTime - savedTimestamp < EXPIRATION_TIME
    }

    private fun clearDataToken(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        editor.remove("idUser")
        editor.remove("token")
        editor.remove("refreshToken")
        editor.remove("timestamp")
        editor.apply()
    }

    companion object {
        private const val EXPIRATION_TIME: Long = 24 * 60 * 60 * 1000 // 24 hours
    }

}
