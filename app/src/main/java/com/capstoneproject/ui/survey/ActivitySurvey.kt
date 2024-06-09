package com.capstoneproject.ui.survey

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import com.capstoneproject.data.source.remote.network.UpdateSurveyStatusRequest
import com.capstoneproject.data.source.remote.network.UpdateSurveyStatusResponse
import com.capstoneproject.databinding.ActivitySurveyBinding
import com.capstoneproject.ui.main.MainActivity
import com.capstoneproject.ui.survey.adapter.SurveyAdapter
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.launch
import retrofit2.Response

class ActivitySurvey : AppCompatActivity(), SurveyAdapter.OnSubmitClickListener {

    private lateinit var binding: ActivitySurveyBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        val viewPager: ViewPager2 = binding.viewPager
        val progressIndicator: LinearProgressIndicator = binding.progressIndicator

        val surveyAdapter = SurveyAdapter(this)
        viewPager.adapter = surveyAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                progressIndicator.progress = position + 1
            }
        })

        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onSubmitClick(position: Int) {
        val viewPager: ViewPager2 = binding.viewPager
        if (position < 3) {
            viewPager.currentItem = position + 1
        } else {
            Toast.makeText(this, "Survey selesai. Terima kasih!", Toast.LENGTH_LONG).show()
            updateSurveyStatus()
        }
    }
    private fun updateSurveyStatus() {
        lifecycleScope.launch {
            try {
                val userId = sharedPreferences.getString("userId", "") ?: return@launch
                val token = sharedPreferences.getString("token", "") ?: return@launch

                val request = UpdateSurveyStatusRequest(userId, true)
                val response: Response<UpdateSurveyStatusResponse> = RetrofitInstance.api.updateSurveyStatus("Bearer $token", userId, request)

                if (response.isSuccessful && response.body()?.status == true) {
                    sharedPreferences.edit().putBoolean("isFillSurvey", true).apply()
                    val intent = Intent(this@ActivitySurvey, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@ActivitySurvey, "Failed to update survey status", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ActivitySurvey, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}