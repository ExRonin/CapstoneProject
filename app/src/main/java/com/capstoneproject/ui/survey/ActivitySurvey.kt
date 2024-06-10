package com.capstoneproject.ui.survey

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.capstoneproject.R
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import com.capstoneproject.data.source.remote.network.UpdateSurveyStatusRequest
import com.capstoneproject.data.source.remote.network.UpdateSurveyStatusResponse
import com.capstoneproject.data.source.remote.network.UserPreferencesRequest
import com.capstoneproject.data.source.remote.network.UserPreferencesResponse
import com.capstoneproject.databinding.ActivitySurveyBinding
import com.capstoneproject.ui.main.MainActivity
import com.capstoneproject.ui.survey.adapter.SurveyAdapter
import com.capstoneproject.utils.showDialogLoading
import com.capstoneproject.utils.showDialogSuccess
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.launch
import retrofit2.Response

class ActivitySurvey : AppCompatActivity(), SurveyAdapter.OnSubmitClickListener {

    private lateinit var binding: ActivitySurveyBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dialogLoading: AlertDialog
    private val answers = HashMap<Int, Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        dialogLoading = showDialogLoading(this)
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

    override fun onSubmitClick(position: Int, data: Any? , bundle: Bundle) {
        answers[position] = data
        val viewPager: ViewPager2 = binding.viewPager
        if (position < 3) {
            viewPager.currentItem = position + 1
        } else {
            dialogLoading.show()
            Handler(Looper.getMainLooper()).postDelayed({
                dialogLoading.dismiss()
                val dialogSuccess = showDialogSuccess(
                    this,
                    getString(R.string.survey_selesai_terima_kasih)
                )
                dialogSuccess.show()
                sendSurveyData(bundle)
            }, 1500)
        }
    }
    private fun sendSurveyData(bundle: Bundle) {
        val userId = sharedPreferences.getString("userId", "") ?: return
        val token = sharedPreferences.getString("token", "") ?: return

        val question1 = bundle.getString("question1")!!.toBoolean()
        val question2 = bundle.getString("question2")!!
        val question3 = bundle.getString("question3")!!
        val question4 = bundle.getStringArrayList("question4")!!


        val request = UserPreferencesRequest(
            userId = userId,
            question1 = question1,
            question2 = question2,
            question3 = question3,
            question4 = question4
        )

        lifecycleScope.launch {
            try {
                val response: Response<UserPreferencesResponse> = RetrofitInstance.api.updateUserPreferences("Bearer $token", request)
                if (response.isSuccessful && response.body()?.status == true) {
                    updateSurveyStatus()
                } else {
                    Toast.makeText(this@ActivitySurvey, "Failed to send survey data", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ActivitySurvey, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
            }
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