package com.capstoneproject.ui.bookingprocess.bookingdetail

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstoneproject.R
import com.capstoneproject.data.Resource
import com.capstoneproject.data.model.order.CreateOrderRequest
import com.capstoneproject.data.model.product.Product
import com.capstoneproject.databinding.FragmentBookingDetailBinding
import com.capstoneproject.ui.bookingprocess.bookingdetail.edit_profile.EditProfileOrdererActivity
import com.capstoneproject.ui.bookingprocess.bookingreceipt.BookingReceiptFragment
import com.capstoneproject.ui.main.MainActivity
import com.capstoneproject.ui.survey.ActivitySurvey
import com.capstoneproject.utils.calculateDaysBetween
import com.capstoneproject.utils.formatCurrency
import com.capstoneproject.utils.showDialogError
import com.capstoneproject.utils.showDialogLoading
import com.capstoneproject.utils.showDialogSuccess
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class BookingDetailFragment : Fragment() {

    private var _binding: FragmentBookingDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var bookingDetailViewModel: BookingDetailViewModel
    private var userFullName = ""
    private var userEmail = ""
    private var userPhone = ""
    private var idUser = ""
    private var token = ""
    private var refreshToken = ""
    private lateinit var product: Product
    private lateinit var myStartBookedCalendar: Calendar
    private lateinit var myEndBookedCalendar: Calendar
    private var bookedAmount: Long = 0L
    private var bookingFeeAndTax = 0
    private var totalPayment = 0.0
    private var paymentBank = ""
    private lateinit var dialogLoading: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingDetailBinding.inflate(inflater, container, false)

        myStartBookedCalendar = Calendar.getInstance()
        myEndBookedCalendar = Calendar.getInstance()
        bookingDetailViewModel = ViewModelProvider(this)[BookingDetailViewModel::class.java]
        dialogLoading = showDialogLoading(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataProduct()
        getIdAndToken()

        setInformationBooking()
        onAction()
    }

    private fun getIdAndToken() {
        bookingDetailViewModel.getDataToken()
        bookingDetailViewModel.idUser.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                idUser = response
            }
        })
        bookingDetailViewModel.token.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                token = response
            }
        })
        bookingDetailViewModel.refreshToken.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                refreshToken = response
                observeDataUser()
            }
        })
    }

    private fun observeDataUser() {
        if (token != "" && idUser != "") {
            bookingDetailViewModel.getDetailUser(idUser = idUser, token = token)
            bookingDetailViewModel.detailUserResponse.observe(viewLifecycleOwner,
                Observer { response ->
                    when (response) {
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(), response.message.toString(), Toast.LENGTH_SHORT
                            ).show()
                        }

                        is Resource.Loading -> {}

                        is Resource.Success -> {
                            userFullName = response.data?.data?.fullname ?: ""
                            userPhone = response.data?.data?.phone ?: ""
                            userEmail = response.data?.data?.email ?: ""
                            binding.apply {
                                tvFullnameUser.text = userFullName
                                tvPhoneUser.text = userPhone
                                tvEmailUser.text = userEmail
                            }
                        }
                    }
                })
        }
    }

    private fun getDataProduct() {
        arguments?.let {
            product = it.getParcelable("extra_product")!!
        }


    }

    private fun setInformationBooking() {
        binding.apply {
            tvFullnameUser.text = userFullName
            tvEmailUser.text = userEmail
            tvPhoneUser.text = userPhone
            tvTotalPayment.text = "Rp ${formatCurrency(product.price)}"
            tvProductSize.text = "${product.height} x ${product.width}"
            tvProductType.text = product.type
            tvProductAddress.text = product.locationDesc
            tvProductName.text = product.name
            tvProductNameAndPrice.text = product.name
            tvProductPrice.text = "Rp ${formatCurrency(product.price)} ($bookedAmount x)"
            tvTotalPayment.text =
                "Rp ${formatCurrency((product.price * bookedAmount).toInt() + 50000 + 1000000)}"

            Glide.with(this@BookingDetailFragment).load(product.imageUrl).into(ivProduct)
        }
    }

    private fun onAction() {
        binding.apply {
            btnEditProfile.setOnClickListener {
                val intent = Intent(requireContext(), EditProfileOrdererActivity::class.java)
                intent.putExtra("userFullName", userFullName)
                intent.putExtra("userEmail", userEmail)
                intent.putExtra("userPhone", userPhone)
                @Suppress("DEPRECATION") startActivityForResult(intent, REQUEST_CODE)
            }

            radioDataChange()

            btnBooking.setOnClickListener {
                var startBooked = etStartBooked.text.toString().trim()
                var endBooked = etEndBooked.text.toString().trim()

                if (!rbBca.isChecked && !rbBri.isChecked && !rbMandiri.isChecked) {
                    rbBca.setError(getString(R.string.pilih_metode_pembayaran))
                    rbBri.setError(getString(R.string.pilih_metode_pembayaran))
                    rbMandiri.setError(getString(R.string.pilih_metode_pembayaran))
                } else {
                    if (checkValidation(
                            startBooked,
                            endBooked,
                            userFullName,
                            userEmail,
                            userPhone,
                        )
                    ) {
                        createOrder(startBooked, endBooked)
                    }
                }
            }

            ivStartBooked.setOnClickListener {
                showStartBookedDatePicker()
            }

            ivEndBooked.setOnClickListener {
                showEndBookedDatePicker()
            }
        }
    }

    private fun createOrder(startBooked: String, endBooked: String) {
        var createOrderRequest = CreateOrderRequest(
            imageProduct = product.imageUrl,
            endBooked = endBooked,
            productId = product.id,
            userId = idUser,
            productName = product.name,
            locationProduct = product.locationDesc,
            startBooked = startBooked,
            categoryProduct = product.category,
            totalPayment = totalPayment,
            phone = userPhone,
            paymentMethod = paymentBank,
            fullname = userFullName,
            email = userEmail
        )
        bookingDetailViewModel.createOrder(token = token, createOrderRequest = createOrderRequest)
        bookingDetailViewModel.createOrderResponse.observe(
            viewLifecycleOwner,
            Observer { response ->
                when (response) {
                    is Resource.Error -> {
                        dialogLoading.dismiss()
                        showDialogError(requireContext(), response.message.toString())
                    }

                    is Resource.Loading -> {
                        dialogLoading.show()
                    }

                    is Resource.Success -> {
                        dialogLoading.dismiss()
                        val dialogSuccess = showDialogSuccess(
                            requireContext(),
                            getString(R.string.selamat_order_anda_berhasil)
                        )
                        dialogSuccess.show()

                        Handler(Looper.getMainLooper())
                            .postDelayed({
                                dialogSuccess.dismiss()
                                val fragmentManager = parentFragmentManager
                                val fragmentTransaction = fragmentManager.beginTransaction()
                                val fragment = BookingReceiptFragment().apply {
                                    arguments = Bundle().apply {
                                        putParcelable("order_response", response.data?.data)
                                    }
                                }
                                fragmentTransaction.replace(
                                    R.id.fragment_container,
                                    fragment
                                )
                                fragmentTransaction.commit()
                            }, 1500)
                    }
                }
            })
    }

    private fun checkValidation(
        startBooked: String,
        endBooked: String,
        userFullName: String,
        userEmail: String,
        userPhone: String,
    ): Boolean {
        binding.apply {
            when {
                startBooked.isEmpty() -> {
                    etStartBooked.error = getString(R.string.silahkan_masukkan_tanggal_awal_pesanan)
                    etStartBooked.requestFocus()
                }

                endBooked.isEmpty() -> {
                    etEndBooked.error = getString(R.string.silahkan_masukkan_tanggal_akhir_pesanan)
                    etEndBooked.requestFocus()
                }

                userFullName.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.silahkan_isi_identitas_pemesan), Toast.LENGTH_SHORT
                    ).show()
                }

                userEmail.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.silahkan_isi_identitas_pemesan), Toast.LENGTH_SHORT
                    ).show()
                }

                userPhone.isEmpty() -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.silahkan_isi_identitas_pemesan), Toast.LENGTH_SHORT
                    ).show()
                }

                else -> return true
            }
        }
        return false
    }

    private fun radioDataChange() {
        binding.radioGroup1.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_bca -> {
                    paymentBank = "BCA"
                    removeErrorNotif()
                }

                R.id.rb_mandiri -> {
                    paymentBank = "Mandiri"
                    removeErrorNotif()
                }

                R.id.rb_bri -> {
                    paymentBank = "BRI"
                    removeErrorNotif()
                }
            }
        }
    }

    private fun removeErrorNotif() {
        binding.rbBca.setError(null)
        binding.rbBri.setError(null)
        binding.rbMandiri.setError(null)
    }


    private fun showEndBookedDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            EndBookedDateCalendar,
            myEndBookedCalendar.get(Calendar.YEAR),
            myEndBookedCalendar.get(Calendar.MONTH),
            myEndBookedCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private val EndBookedDateCalendar =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myEndBookedCalendar.set(Calendar.YEAR, year)
            myEndBookedCalendar.set(Calendar.MONTH, month)
            myEndBookedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLableEndBooked(myEndBookedCalendar)
        }

    private fun updateLableEndBooked(myEndBookedCalendar: Calendar) {
        val myFormat = "MMM dd, yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        binding.apply {
            etEndBooked.setText(sdf.format(myEndBookedCalendar.time))
            if (etStartBooked.text.toString() != "" && etEndBooked.text.toString() != "") {
                showPriceData()
            }
        }
    }

    private fun showPriceData() {
        binding.apply {
            tvProductBookingDate.text = "${etStartBooked.text} s/d ${etEndBooked.text}"
            bookedAmount = calculateDaysBetween(
                etStartBooked.text.toString(), etEndBooked.text.toString()
            )
            bookingFeeAndTax = (bookedAmount * product.price * 0.12 + 100000).toInt()
            tvProductTax.text = bookingFeeAndTax.toString()
            tvProductBookingTime.text = "$bookedAmount hari"
            tvProductPrice.text = "Rp ${formatCurrency(product.price)} ($bookedAmount x)"
            totalPayment = ((product.price * bookedAmount) + 50000 + bookingFeeAndTax).toDouble()
            tvTotalPayment.text = "Rp ${formatCurrency(totalPayment.toInt())}"
        }
    }

    private fun showStartBookedDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            startBookedDateCalendar,
            myStartBookedCalendar.get(Calendar.YEAR),
            myStartBookedCalendar.get(Calendar.MONTH),
            myStartBookedCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private val startBookedDateCalendar =
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myStartBookedCalendar.set(Calendar.YEAR, year)
            myStartBookedCalendar.set(Calendar.MONTH, month)
            myStartBookedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLableStartBooked(myStartBookedCalendar)
        }

    private fun updateLableStartBooked(myStartBookedCalendar: Calendar) {
        val myFormat = "MMM dd, yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        binding.apply {
            etStartBooked.setText(sdf.format(myStartBookedCalendar.time))
            if (etStartBooked.text.toString() != "" && etEndBooked.text.toString() != "") {
                showPriceData()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION") super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            userFullName = data?.getStringExtra("userFullNameReturn").toString()
            userPhone = data?.getStringExtra("userPhoneReturn").toString()
            userEmail = data?.getStringExtra("userEmailReturn").toString()
            binding.apply {
                tvFullnameUser.text = userFullName
                tvPhoneUser.text = userPhone
                tvEmailUser.text = userEmail
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_CODE = 1
    }

}