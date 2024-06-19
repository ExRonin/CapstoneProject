package com.capstoneproject.ui.search

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstoneproject.R
import com.capstoneproject.data.model.product.Product
import com.capstoneproject.data.source.remote.network.RetrofitInstance
import com.capstoneproject.databinding.FragmentSearchBinding
import com.capstoneproject.ui.detailproduct.DetailProductActivity
import com.capstoneproject.ui.search.adapter.RecyclerViewAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var geocoder: Geocoder
    private lateinit var googleMap: GoogleMap
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    // List to store markers on the map
    private val mapMarkers = mutableListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        geocoder = Geocoder(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.etSearchMain.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                navigateToSearchResults()
            }

            binding.etSearchMain.isFocusable = false
        }

        recyclerView = binding.recyclerViewSearch
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerView.adapter = recyclerViewAdapter


        return view
    }

    private fun navigateToSearchResults() {
        findNavController().navigate(R.id.action_searchFragment_to_searchResultsFragment)
    }




    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        setMapStyle()
        fetchProducts()
        onAction()
    }

    private fun vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
        val vectorDrawable = ResourcesCompat.getDrawable(resources, id, null)
            ?: return BitmapDescriptorFactory.defaultMarker()
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun onAction() {
        recyclerViewAdapter.setOnItemClickCallback(object : RecyclerViewAdapter.OnItemClickCallback {
            override fun onItemClicked(product: Product) {
                val intent = Intent(requireContext(), DetailProductActivity::class.java)
                intent.putExtra("extra_product", product)
                startActivity(intent)
            }
        })
    }

    private fun fetchProducts() = lifecycleScope.launch {
        val token = sharedPreferences.getString("token", null)
        if (token.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Authorization token is missing", Toast.LENGTH_SHORT).show()
            return@launch
        }

        try {
            val response = RetrofitInstance.api.getProductsByUserPreferences("Bearer $token")
            if (response.isSuccessful) {
                val products = response.body()?.data ?: emptyList()
                addMarkersToMap(products)
                recyclerViewAdapter.setListProduct(products)
                binding.recyclerViewSearch.apply {
                    layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    adapter = recyclerViewAdapter
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addMarkersToMap(products: List<Product>) {
        for (product in products) {
            val markerOptions = MarkerOptions()
                .position(LatLng(product.latitude, product.longitude))
                .title(product.name)
                .snippet(product.description)
                .icon(vectorToBitmap(R.drawable.icon_baliho, Color.parseColor("#8e4585")))
            googleMap.addMarker(markerOptions)
        }

        val builder = LatLngBounds.Builder()
        for (product in products) {
            builder.include(LatLng(product.latitude, product.longitude))
        }
        val bounds = builder.build()
        val padding = 100
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap.animateCamera(cameraUpdate)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}
