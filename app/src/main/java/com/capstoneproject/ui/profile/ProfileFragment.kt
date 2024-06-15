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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.capstoneproject.R
import com.capstoneproject.data.Resource
import com.capstoneproject.databinding.FragmentProfileBinding
import com.capstoneproject.ui.login.LoginActivity
import com.capstoneproject.utils.gone
import com.capstoneproject.utils.invisible
import com.capstoneproject.utils.showDialogError
import com.capstoneproject.utils.showDialogLoading
import com.capstoneproject.utils.showDialogSuccess
import com.capstoneproject.utils.visible

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var dialogLoading: AlertDialog
    private var idUser = ""
    private var token = ""
    private var refreshToken = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogLoading = showDialogLoading(requireContext())
        getIdAndToken()
        onAction()
    }

    private fun observeDataUser() {
        binding.apply {
            if (token != "" && idUser != "") {
                profileViewModel.getDetailUser(idUser = idUser, token = token)
                profileViewModel.detailUserResponse.observe(viewLifecycleOwner, Observer { response ->
                    when (response) {
                        is Resource.Error -> {
                            pgProfile.gone()
                            ivUser.invisible()
                            showDialogError(requireContext(), response.message.toString())
                        }
                        is Resource.Loading -> {
                            ivUser.invisible()
                            pgProfile.gone()
                        }
                        is Resource.Success -> {
                            ivUser.visible()
                            val user = response.data?.data
                            if (user != null) {
                                tvFullnameUser.text = user.fullname
                                tvEmailUser.text = user.email
                            }
                        }
                    }
                })
            }
        }
    }

    private fun getIdAndToken() {
        profileViewModel.getDataToken()
        profileViewModel.idUser.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                idUser = response
            }
        })
        profileViewModel.token.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                token = response
            }
        })
        profileViewModel.refreshToken.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                refreshToken = response
                observeDataUser()
            }
        })
    }

    private fun onAction() {
        binding.apply {
            containerLogout.setOnClickListener {
                logoutDialog()
            }
        }
    }

    private fun logoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.keluar))
        builder.setMessage(getString(R.string.apakah_anda_yakin_ingin_keluar))
        builder.setPositiveButton(getString(R.string.iya)) { _, _ ->
            logout()
        }
        builder.setNegativeButton(getString(R.string.tidak)) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun logout() {
        if (token != "") {
            profileViewModel.logoutUser(token)
            profileViewModel.logoutResponse.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Resource.Error -> {
                        dialogLoading.dismiss()
                        showDialogError(requireContext(), response.message.toString())
                    }

                    is Resource.Loading -> {
                        dialogLoading.show()
                    }

                    is Resource.Success -> {
                        profileViewModel.clearDataToken()
                        dialogLoading.dismiss()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        activity?.finishAffinity()
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ProfileFragment"
    }


}