package com.capstoneproject.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.capstoneproject.R
import com.capstoneproject.databinding.FragmentFilterBinding
class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        val imageViews = arrayOf(
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
        val imageLabels = arrayOf(
            "Baliho", "Bando Jalan", "Bilboard", "Digital Ads",
            "Videotron/LED", "Neon Box", "Digital Signature", "Transportasi",
            "Banner", "Lift", "Eskalator", "Wall Branding"
        )
        val selectedImages = HashSet<Int>() // Daftar indeks gambar yang dipilih

        bind(imageViews, selectedImages)

        binding.leftIcon.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun bind(imageViews: Array<ImageView>, selectedImages: HashSet<Int>) {
        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                toggleImageViewSelection(index, imageView, selectedImages)
                checkSubmitButtonState()
            }
            imageView.setImageResource(getImageResourceByIndex(index))
        }
    }

    private fun toggleImageViewSelection(index: Int, imageView: ImageView, selectedImages: HashSet<Int>) {
        if (selectedImages.contains(index)) {
            imageView.isSelected = false
            imageView.setImageResource(getImageResourceByIndex(index))
            selectedImages.remove(index)
        } else {
            imageView.isSelected = true
            imageView.setImageResource(getSelectedImageResourceByIndex(index))
            selectedImages.add(index)
        }
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

    private fun checkSubmitButtonState() {
        // Implement logic to enable/disable submit button based on selected images
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}