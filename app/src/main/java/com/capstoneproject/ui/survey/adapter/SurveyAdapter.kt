package com.capstoneproject.ui.survey.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
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
            R.layout.page_survey_1 -> Survey1ViewHolder(
                PageSurvey1Binding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            R.layout.page_survey_2 -> Survey2ViewHolder(
                PageSurvey2Binding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            R.layout.page_survey_3 -> Survey3ViewHolder(
                PageSurvey3Binding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            R.layout.page_survey_4 -> Survey4ViewHolder(
                PageSurvey4Binding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

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

    inner class Survey1ViewHolder(private val binding: PageSurvey1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.yesButton.setOnClickListener {
                setButtonSelected(binding.yesButton)
                updateButtonState(true, binding.yesButton)
                updateButtonState(false, binding.noButton)
                checkSubmitButtonState()
            }

            binding.noButton.setOnClickListener {
                setButtonSelected(binding.noButton)
                updateButtonState(true, binding.noButton)
                updateButtonState(false, binding.yesButton)
                checkSubmitButtonState()
            }

            binding.submitButton.setOnClickListener {
                onSubmitClickListener.onSubmitClick(position)
            }


            checkSubmitButtonState()

        }

        private fun setButtonSelected(button: Button) {
            binding.yesButton.isSelected = button == binding.yesButton
            binding.noButton.isSelected = button == binding.noButton
        }

        private fun updateButtonState(isSelected: Boolean, button: Button) {
            if (isSelected) {
                button.setTextColor(ContextCompat.getColor(button.context, android.R.color.black))
            } else {
                button.setTextColor(ContextCompat.getColor(button.context, android.R.color.white))
            }
        }

        private fun checkSubmitButtonState() {
            val isEnabled = binding.yesButton.isSelected || binding.noButton.isSelected
            binding.submitButton.isEnabled = isEnabled
            if (isEnabled) {
                binding.submitButton.setTextColor(
                    ContextCompat.getColor(
                        binding.submitButton.context,
                        android.R.color.white
                    )
                )
            } else {
                binding.submitButton.setTextColor(
                    ContextCompat.getColor(
                        binding.submitButton.context,
                        android.R.color.black
                    )
                )
            }
        }

    }

    inner class Survey2ViewHolder(private val binding: PageSurvey2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        private val buttonIds =
            arrayOf(R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5)

        fun bind(position: Int) {
            buttonIds.forEach { buttonId ->
                val button = binding.root.findViewById<Button>(buttonId)
                button.setOnClickListener {
                    setButtonSelected(button)
                    updateButtonState(true, button)
                    buttonIds.filter { it != buttonId }.forEach { otherButtonId ->
                        updateButtonState(false, binding.root.findViewById<Button>(otherButtonId))
                    }
                    checkSubmitButtonState()
                }
            }

            binding.submitButton.setOnClickListener {

                onSubmitClickListener.onSubmitClick(position)
            }

            checkSubmitButtonState()
        }

        private fun setButtonSelected(button: Button) {
            buttonIds.forEach { otherButtonId ->
                val otherButton = binding.root.findViewById<Button>(otherButtonId)
                otherButton.isSelected = otherButton == button
            }
        }

        private fun updateButtonState(isSelected: Boolean, button: Button) {
            if (isSelected) {
                button.setTextColor(ContextCompat.getColor(button.context, android.R.color.black))
            } else {
                button.setTextColor(ContextCompat.getColor(button.context, android.R.color.white))
            }
        }

        private fun checkSubmitButtonState() {
            val isEnabled = buttonIds.any { binding.root.findViewById<Button>(it).isSelected }
            binding.submitButton.isEnabled = isEnabled
            if (isEnabled) {
                binding.submitButton.setTextColor(
                    ContextCompat.getColor(
                        binding.submitButton.context,
                        android.R.color.white
                    )
                )
            } else {
                binding.submitButton.setTextColor(
                    ContextCompat.getColor(
                        binding.submitButton.context,
                        android.R.color.black
                    )
                )
            }
        }
    }


    inner class Survey3ViewHolder(private val binding: PageSurvey3Binding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageViews = arrayOf(
            binding.image1,
            binding.image2,
            binding.image3,
            binding.image4,
            binding.image5,
            binding.image6
        )

        fun bind(position: Int) {
            imageViews.forEachIndexed { _, imageView ->
                imageView.setOnClickListener {
                    toggleImageViewSelection(imageView)
                    checkSubmitButtonState()
                }
            }

            binding.submitButton.setOnClickListener {
                onSubmitClickListener.onSubmitClick(position)
            }

            // Inisialisasi status tombol submit
            checkSubmitButtonState()
        }

        private fun toggleImageViewSelection(selectedImageView: ImageView) {
            imageViews.forEach { imageView ->
                if (imageView != selectedImageView) {
                    imageView.setImageResource(getImageResourceByIndex(imageViews.indexOf(imageView)))
                    imageView.isSelected = false
                }
            }

            if (selectedImageView.isSelected) {
                selectedImageView.setImageResource(
                    getImageResourceByIndex(
                        imageViews.indexOf(
                            selectedImageView
                        )
                    )
                )
                selectedImageView.isSelected = false
            } else {
                selectedImageView.setImageResource(
                    getSelectedImageResourceByIndex(
                        imageViews.indexOf(
                            selectedImageView
                        )
                    )
                )
                selectedImageView.isSelected = true
            }
        }


        private fun getImageResourceByIndex(index: Int): Int {
            return when (index) {
                0 -> R.drawable.kota1
                1 -> R.drawable.kota2
                2 -> R.drawable.kota3
                3 -> R.drawable.kota4
                4 -> R.drawable.kota5
                5 -> R.drawable.kota6
                else -> R.drawable.kota1
            }
        }

        private fun getSelectedImageResourceByIndex(index: Int): Int {
            return when (index) {
                0 -> R.drawable.kota1selector
                1 -> R.drawable.kota2selector
                2 -> R.drawable.kota3selector
                3 -> R.drawable.kota4selector
                4 -> R.drawable.kota5selector
                5 -> R.drawable.kota6selector
                else -> R.drawable.kota1selector
            }
        }

        private fun checkSubmitButtonState() {
            val isAnySelected = imageViews.any { it.isSelected }
            binding.submitButton.isEnabled = isAnySelected
            if (isAnySelected) {
                binding.submitButton.setTextColor(
                    ContextCompat.getColor(
                        binding.submitButton.context,
                        android.R.color.white
                    )
                )
            } else {
                binding.submitButton.setTextColor(
                    ContextCompat.getColor(
                        binding.submitButton.context,
                        android.R.color.black
                    )
                )
            }
        }
    }


    inner class Survey4ViewHolder(private val binding: PageSurvey4Binding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageViews = arrayOf(
            binding.image1,
            binding.image2,
            binding.image3,
            binding.image4,
            binding.image5,
            binding.image6,
            binding.image7,
            binding.image8,
            binding.image9,
            binding.image10,
            binding.image11,
            binding.image12
        )

        private val selectedImages = HashSet<Int>() // Daftar indeks gambar yang dipilih

        fun bind(position: Int) {
            imageViews.forEachIndexed { index, imageView ->
                imageView.setOnClickListener {
                    toggleImageViewSelection(index)
                    checkSubmitButtonState()
                }
            }

            binding.submitButton.setOnClickListener {
                onSubmitClickListener.onSubmitClick(position)
            }


            checkSubmitButtonState()
        }



        private fun getImageResourceByIndex(index: Int): Int {
            return when (index) {
                0 -> R.drawable.box1
                1 -> R.drawable.box2
                2 -> R.drawable.box3
                3 -> R.drawable.box4
                4 -> R.drawable.box5
                5 -> R.drawable.box6
                6 -> R.drawable.box7
                7 -> R.drawable.box8
                8 -> R.drawable.box9
                9 -> R.drawable.box10
                10 -> R.drawable.box11
                11 -> R.drawable.box12
                else -> R.drawable.box1
            }
        }


        private fun getSelectedImageResourceByIndex(index: Int): Int {
            return when (index) {
                0 -> R.drawable.box1s
                1 -> R.drawable.box2s
                2 -> R.drawable.box3s
                3 -> R.drawable.box4s
                4 -> R.drawable.box5s
                5 -> R.drawable.box6s
                6 -> R.drawable.box7s
                7 -> R.drawable.box8s
                8 -> R.drawable.box9s
                9 -> R.drawable.box10s
                10 -> R.drawable.box11s
                11 -> R.drawable.box12s
                else -> R.drawable.box1s
            }
        }

        private fun toggleImageViewSelection(index: Int) {
            val imageView = imageViews[index]
            if (imageView.isSelected) {
                imageView.isSelected = false
                imageView.setImageResource(getImageResourceByIndex(index))
                selectedImages.remove(index)
            } else {
                imageView.isSelected = true
                imageView.setImageResource(getSelectedImageResourceByIndex(index))
                selectedImages.add(index)
            }
        }

        private fun checkSubmitButtonState() {
            val isAnySelected = selectedImages.isNotEmpty()
            binding.submitButton.isEnabled = isAnySelected
            binding.submitButton.setTextColor(
                ContextCompat.getColor(
                    binding.submitButton.context,
                    if (isAnySelected) android.R.color.white else R.color.black
                )
            )
        }
    }
}