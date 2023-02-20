package com.example.travelcatalogapp.ui.home

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
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
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {

    @Inject
    lateinit var session: Session

    private var tour = ArrayList<Tour?>()
//    private var tourAll = ArrayList<Tour?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageList = ArrayList<SlideModel>()

        observe()
        getTourList()
        getImage()


        val user = session.getUser()
        if (user != null) {
            binding.user = user
        }

//        //SearchView Function
//        binding?.searchView?.doOnTextChanged { text, start, before, count ->
//            if (text!!.isNotEmpty()) {
//                val filter = tourAll.filter { it?.name?.contains("$text", true) == true }
////                val filteringData =
////                    noteAll.filter { it?.note?.contains(text.toString(), true) == true }
//                Log.d("CekFilter", "Keyword $text Data : $filter")
//                tour.clear()
////                note.addAll(filter)
//                filter.forEach {
//                    tour.add(it)
//                }
////                binding?.rvNote?.adapter?.notifyDataSetChanged()
//                binding?.rvHome?.adapter?.notifyItemInserted(0)
//
//            }
//            //            else {
////                note.clear()
////                binding?.rvNote?.adapter?.notifyDataSetChanged()
////                note.addAll(noteAll)
////                Log.d("ceknoteall", "noteall : $noteAll")
////                binding?.rvNote?.adapter?.notifyItemInserted(0)
////            }
//        }

        //Button Image View
        binding.ivProfile.setOnClickListener {
            openActivity<ProfileActivity>()
        }

        //Button Category Nature
        binding.btnNature.setOnClickListener {
            openActivity<ListActivity>{
                putExtra(Cons.CATEGORY.ID,1)
            }
        }


        //Button Category Park
        binding.btnPark.setOnClickListener {
            openActivity<ListActivity>{
                putExtra(Cons.CATEGORY.ID,2)
            }
        }
        //Button Category All
        binding.btnAll.setOnClickListener {
            openActivity<ListActivity> {
                putExtra(Cons.CATEGORY.ID,3)
            }
        }

        //Button View All
        binding.tvViewAll.setOnClickListener {
            openActivity<ListActivity>{
                putExtra(Cons.CATEGORY.ID,3)
            }
        }

        //Adapter List Tour
        binding.rvHome.adapter = CoreListAdapter<ItemHomeBinding, Tour>(R.layout.item_home)
            .initItem(tour) { position, data ->
                openActivity<DetailActivity> {
                    putExtra(Cons.TOUR.TOUR, data)
                }
            }
        //Preview Image Profile
        Glide
            .with(this)
            .load(user?.image)
            .into(binding.ivProfile)

//        Preview Image Slider
//        imageList.add(SlideModel("https://magang.crocodic.net/ki/kelompok_3/tour-app/public/image/qacdaVHctsSD3xZfdpBoSq1lfVTZpFEYs55Uypzf.jpg"))
//        imageList.add(SlideModel("https://magang.crocodic.net/ki/kelompok_3/tour-app/public/image/fXl0LUhqAZ1jS4vOOGvQ7oX7fJ6LlwcRwbGB6HCI.jpg"))

        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        imageSlider.setImageList(imageList)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    getTourList()
                }
            }
        }



    }

//     fun onQueryTextSubmit(query: String?): Boolean {
//        return true
//    }
//
//
//     fun onQueryTextChange(newText: String?): Boolean {
//        Log.d("Keyword", "$newText")
//        return false
//    }

    //Get Note Function
    private fun getTourList() {
        viewModel.tourList()
    }

    private fun observe(){
        viewModel.tour.observe(this){
            tour.addAll(it)
            binding.rvHome.adapter?.notifyDataSetChanged()
        }

        viewModel.image.observe(this){
            initSlider(it)
        }
    }

    fun getImage(){
        viewModel.imageSlider()
    }

    private fun initSlider(data: List<ImageSlide>) {
        val imageList = ArrayList<SlideModel>()
        data.forEach{
            imageList.add(SlideModel(it.image))
        }
        binding.imageSlider.setImageList(imageList,ScaleTypes.CENTER_CROP)
    }



}