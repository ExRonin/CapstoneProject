package com.capstoneproject.ui.survey.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.R
import com.capstoneproject.databinding.PageSurvey1Binding
import com.capstoneproject.databinding.PageSurvey2Binding
import com.capstoneproject.databinding.PageSurvey3Binding
import com.capstoneproject.databinding.PageSurvey4Binding

class SurveyAdapter(private val onSubmitClickListener: OnSubmitClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val layoutIds = listOf(
        R.layout.page_survey_1,
        R.layout.page_survey_2,
        R.layout.page_survey_3,
        R.layout.page_survey_4
    )

    interface OnSubmitClickListener {
        fun onSubmitClick(position: Int)
    }

    override fun getItemViewType(position: Int): Int {
        return layoutIds[position]
    }

    override fun getItemCount(): Int {
        return layoutIds.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.page_survey_1 -> Survey1ViewHolder(PageSurvey1Binding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.page_survey_2 -> Survey2ViewHolder(PageSurvey2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.page_survey_3 -> Survey3ViewHolder(PageSurvey3Binding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.page_survey_4 -> Survey4ViewHolder(PageSurvey4Binding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is Survey1ViewHolder -> holder.bind(position)
            is Survey2ViewHolder -> holder.bind(position)
            is Survey3ViewHolder -> holder.bind(position)
            is Survey4ViewHolder -> holder.bind(position)
        }
    }

    inner class Survey1ViewHolder(private val binding: PageSurvey1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.submitButton.setOnClickListener {
                onSubmitClickListener.onSubmitClick(position)
            }
        }
    }

    inner class Survey2ViewHolder(private val binding: PageSurvey2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.submitButton.setOnClickListener {
                onSubmitClickListener.onSubmitClick(position)
            }
        }
    }

    inner class Survey3ViewHolder(private val binding: PageSurvey3Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.submitButton.setOnClickListener {
                onSubmitClickListener.onSubmitClick(position)
            }
        }
    }

    inner class Survey4ViewHolder(private val binding: PageSurvey4Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.submitButton.setOnClickListener {
                onSubmitClickListener.onSubmitClick(position)
            }
        }
    }
}