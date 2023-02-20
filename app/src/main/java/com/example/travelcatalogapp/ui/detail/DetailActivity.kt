package com.example.travelcatalogapp.ui.detail

import android.os.Bundle
import com.example.travelcatalogapp.R
import com.example.travelcatalogapp.base.BaseActivity
import com.example.travelcatalogapp.data.Cons
import com.example.travelcatalogapp.data.Tour
import com.example.travelcatalogapp.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding,DetailViewModel>(R.layout.activity_detail) {

    private var tour :Tour? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Reciving TourData
        tour = intent.getParcelableExtra(Cons.TOUR.TOUR)
        binding.data = tour

        //Button Back
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}