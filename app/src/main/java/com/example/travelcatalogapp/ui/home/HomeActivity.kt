package com.example.travelcatalogapp.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.base.adapter.CoreListAdapter
import com.crocodic.core.extension.openActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.travelcatalogapp.R
import com.example.travelcatalogapp.base.BaseActivity
import com.example.travelcatalogapp.data.Cons
import com.example.travelcatalogapp.data.ImageSlide
import com.example.travelcatalogapp.data.Session
import com.example.travelcatalogapp.data.Tour
import com.example.travelcatalogapp.databinding.ActivityHomeBinding
import com.example.travelcatalogapp.databinding.ItemHomeBinding
import com.example.travelcatalogapp.ui.detail.DetailActivity
import com.example.travelcatalogapp.ui.list.ListActivity
import com.example.travelcatalogapp.ui.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home),
    SearchView.OnQueryTextListener {

    @Inject
    lateinit var session: Session

    private var tour = ArrayList<Tour?>()
    private var tourAll = ArrayList<Tour?>()
    private var tourClass: Tour? = null


    override fun onStart() {
        val user = session.getUser()
        if (user != null) {
            binding.user = user
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        val user = session.getUser()

        observe()
        getTourList()
        getImage()
        search()

        Handler(Looper.getMainLooper()).postDelayed({
            getTourList()
        },0)

//SearchView Function
        //Button Image View
        binding.ivProfile.setOnClickListener {
            openActivity<ProfileActivity>()
        }


        //Button Category Nature
        binding.btnNature.setOnClickListener {
            openActivity<ListActivity> {
                putExtra(Cons.CATEGORY.ID, 1)
            }
        }


        //Button Category Park
        binding.btnPark.setOnClickListener {
            openActivity<ListActivity> {
                putExtra(Cons.CATEGORY.ID, 2)
            }
        }
        //Button Category All
        binding.btnAll.setOnClickListener {
            openActivity<ListActivity> {
                putExtra(Cons.CATEGORY.ID, 3)
            }
        }

        //Button View All
        binding.tvViewAll.setOnClickListener {
            openActivity<ListActivity> {
                putExtra(Cons.CATEGORY.ID, 4)
            }
        }

        //Adapter List Tour
        binding.rvHome.adapter = CoreListAdapter<ItemHomeBinding, Tour>(R.layout.item_home)
            .initItem(tour) { position, data ->
                openActivity<DetailActivity> {
                    putExtra(Cons.TOUR.TOUR, data)
                }
            }


        //Variable Image slider
        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        imageSlider.setImageList(imageList)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    getTourList()
                }
            }
        }


    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("Keyword", "$newText")
        return false
    }

    //Get Note Function
    private fun getTourList() {
        viewModel.tourList()
    }

    //Function Observe
    private fun observe() {
        viewModel.tour.observe(this) {
            //Before i gave this the data is empty
            tour.clear()
            tourAll.clear()

            tour.addAll(it)
            tourAll.addAll(it)
            binding?.rvHome?.adapter?.notifyDataSetChanged()
            binding?.rvHome?.adapter?.notifyItemInserted(0)
        }

        viewModel.image.observe(this) {
            initSlider(it)
        }
    }

    //Get Image Slider Function
    private fun getImage() {
        viewModel.imageSlider()
    }

    //Init Slider Function
    private fun initSlider(data: List<ImageSlide>) {
        val imageList = ArrayList<SlideModel>()
        data.forEach {
            imageList.add(SlideModel(it.image, tourClass?.name))
        }
        binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
    }

    private fun search() {
        binding.searchView.doOnTextChanged { text, start, before, count ->
            if (text!!.isNotEmpty()) {
                val filter = tourAll.filter { it?.name?.contains("$text", true) == true }
//                val filteringData =
//                    noteAll.filter { it?.note?.contains(text.toString(), true) == true }
                Log.d("CekFilter", "Keyword $text Data : $filter")
                tour.clear()
//                note.addAll(filter)
                filter.forEach {
                    tour.add(it)
                }
                binding?.rvHome?.adapter?.notifyDataSetChanged()
                binding?.rvHome?.adapter?.notifyItemInserted(0)

                if (filter.isEmpty()) {
                    binding.tvDataKosong.visibility = View.VISIBLE
                } else {
                    binding.tvDataKosong.visibility = View.GONE

                }

            } else {
                tour.clear()
                binding?.rvHome?.adapter?.notifyDataSetChanged()
                tour.addAll(tourAll)
                Log.d("checkNoteAll", "noteAll : $tourAll")
                binding?.rvHome?.adapter?.notifyItemInserted(0)
            }
        }

    }

}