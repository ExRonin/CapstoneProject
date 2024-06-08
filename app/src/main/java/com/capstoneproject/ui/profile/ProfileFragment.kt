package com.capstoneproject.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.R
import com.capstoneproject.databinding.FragmentProfileBinding
import com.capstoneproject.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onAction()
    }

    private fun onAction() {
        binding.apply {
            containerLogout.setOnClickListener {
                logoutAction()
            }
        }
    }

    private fun logoutAction() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.keluar))
        builder.setMessage(getString(R.string.apakah_anda_yakin_ingin_keluar))
        builder.setPositiveButton(getString(R.string.iya)) { _, _ ->
            val sharedPreferences = requireContext().getSharedPreferences(
                "app_prefs",
                AppCompatActivity.MODE_PRIVATE
            )
            val editor = sharedPreferences.edit()
            editor.remove("idUser")
            editor.remove("token")
            editor.remove("refreshToken")
            editor.apply()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        builder.setNegativeButton(getString(R.string.tidak)) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ProfileFragment"
    }


}