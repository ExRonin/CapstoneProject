package com.capstoneproject.ui.onboardscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstoneproject.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var title: String
    private lateinit var description: String
    private var imageResource = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_PARAM1)!!
            description = it.getString(ARG_PARAM2)!!
            imageResource = it.getInt(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        binding.textOnboardingTitle.text = title
        binding.textOnboardingDescription.text = description
        binding.imageOnboarding.setImageResource(imageResource)
        return binding.root
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_PARAM3 = "param3"

        @JvmStatic
        fun newInstance(title: String, description: String, imageResource: Int) =
            OnboardingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, title)
                    putString(ARG_PARAM2, description)
                    putInt(ARG_PARAM3, imageResource)
                }
            }
    }
}