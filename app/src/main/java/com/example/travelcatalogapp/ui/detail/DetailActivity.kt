package com.example.travelcatalogapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import com.crocodic.core.extension.tos
import com.example.travelcatalogapp.R
import com.example.travelcatalogapp.base.BaseActivity
import com.example.travelcatalogapp.data.Cons
import com.example.travelcatalogapp.data.Tour
import com.example.travelcatalogapp.databinding.ActivityDetailBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity :
    BaseActivity<ActivityDetailBinding, DetailViewModel>(R.layout.activity_detail) {

    private var tour: Tour? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)


        maplocation()

        //Reciving TourData
        tour = intent.getParcelableExtra(Cons.TOUR.TOUR)
        binding.data = tour

        //Button Back
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnRoute.setOnClickListener {
            sendLocationIntent()
        }

        binding.btnFav.setOnClickListener {
            tos("Tour Liked")
            val tourId = tour?.id
            if (tourId != null) {
                viewModel.favouriteTour(tourId)
            }
        }

        binding.btnUnfav.setOnClickListener {
            tos("Tour Unliked")
            val tourId = tour?.id
            if (tourId != null) {
                viewModel.favouriteTour(tourId)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        binding.mapView.onSaveInstanceState(outState)

    }

    //get Location Mapview
    private fun maplocation() {
        binding.mapView.getMapAsync { map ->
            val lat = tour?.latitude?.toDouble()
            val long = tour?.longitude?.toDouble()

            if (lat != null && long != null) {
                val locationDestination = LatLng(lat, long)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(locationDestination, 15f))
                map.addMarker(
                    MarkerOptions()
                        .position(locationDestination)
                        .title("destination")
                )
            }


        }
    }

    private fun initialButtonLike() {
        if (tour?.like?.equals("true") == true) {
            binding.btnFav.visibility = View.GONE
            binding.btnUnfav.visibility = View.VISIBLE
        }else{
            binding.btnFav.visibility = View.VISIBLE
            binding.btnUnfav.visibility = View.GONE

        }
    }

    //Sending location to Open Google Maps
    private fun sendLocationIntent() {
        val intentUri = Uri.parse("google.navigation:q=${tour?.name}&mode=d")
        val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }


}