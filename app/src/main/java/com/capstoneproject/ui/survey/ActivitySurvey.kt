package com.capstoneproject.ui.survey

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.capstoneproject.databinding.ActivitySurveyBinding
import com.capstoneproject.ui.main.MainActivity
import com.capstoneproject.ui.survey.adapter.SurveyAdapter
import com.google.android.material.progressindicator.LinearProgressIndicator


class ActivitySurvey : AppCompatActivity(), SurveyAdapter.OnSubmitClickListener {

    private lateinit var binding: ActivitySurveyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}