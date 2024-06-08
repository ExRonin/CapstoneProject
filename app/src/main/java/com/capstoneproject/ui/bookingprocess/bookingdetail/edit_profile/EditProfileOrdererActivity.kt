package com.capstoneproject.ui.bookingprocess.bookingdetail.edit_profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.R
import com.capstoneproject.databinding.ActivityEditProfileOrdererBinding

class EditProfileOrdererActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileOrdererBinding
    private lateinit var userFullName: String
    private lateinit var userEmail: String
    private lateinit var userPhone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileOrdererBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiverInformationFromIntent()
        onAction()

    }

    private fun onAction() {
        binding.apply {
            btnCloseEdit.setOnClickListener {
                finish()
            }

            btnCancel.setOnClickListener {
                finish()
            }

            btnSave.setOnClickListener {
                val fullname = etFullName.text.toString()
                val phone = etPhone.text.toString().trim()
                val email = etEmail.text.toString().trim()
                if(checkValidation(fullname, phone, email)) {
                    val returnIntent = Intent()
                    returnIntent.putExtra("userFullNameReturn", fullname)
                    returnIntent.putExtra("userPhoneReturn", phone)
                    returnIntent.putExtra("userEmailReturn", email)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
        }
    }

    private fun checkValidation(fullname: String, phone: String, email: String): Boolean {
        binding.apply {
            when {
                fullname.isEmpty() -> {
                    etFullName.error = getString(R.string.masukkan_nama_lengkap_anda)
                    etFullName.requestFocus()
                }
                phone.isEmpty() -> {
                    etPhone.error = getString(R.string.masukkan_nomor_ponsel_anda)
                    etPhone.requestFocus()
                }
                email.isEmpty() -> {
                    etEmail.error = getString(R.string.masukkan_email_anda)
                    etEmail.requestFocus()
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    etEmail.error = getString(R.string.please_use_a_valid_email)
                    etEmail.requestFocus()
                }
                else -> return true
            }
        }
        return false
    }

    private fun receiverInformationFromIntent() {
        userFullName = intent.getStringExtra("userFullName").toString()
        userEmail = intent.getStringExtra("userEmail").toString()
        userPhone = intent.getStringExtra("userPhone").toString()

        binding.apply {
            etFullName.setText(userFullName)
            etEmail.setText(userEmail)
            etPhone.setText(userPhone)
        }
    }
}