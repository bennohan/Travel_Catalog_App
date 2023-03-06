package com.example.travelcatalogapp.ui.list

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.base.adapter.CoreListAdapter
import com.crocodic.core.extension.openActivity
import com.example.travelcatalogapp.R
import com.example.travelcatalogapp.base.BaseActivity
import com.example.travelcatalogapp.data.Cons
import com.example.travelcatalogapp.data.Tour
import com.example.travelcatalogapp.databinding.ActivityListBinding
import com.example.travelcatalogapp.databinding.ListItemBinding
import com.example.travelcatalogapp.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListActivity : BaseActivity<ActivityListBinding, ListViewModel>(R.layout.activity_list) {

    private var tour = ArrayList<Tour?>()

    override fun onStart() {
        super.onStart()
        getData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe()
        adapter()
        getData()
//        looperData()

        Handler(Looper.getMainLooper()).postDelayed({
            getData()
        },0)

        //Swipe Refresh Layout
        binding.swipeLayout.setOnRefreshListener {
            getData()
        }

        // Button Back
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

    }

    //Function GetData
    private fun getData() {
        val idCategory = intent.getIntExtra(Cons.CATEGORY.ID, 0)
//        viewModel.tourListPath(if ( idCategory > 3) null else idCategory)
        if (idCategory == 4) {
            tourRec()
        } else {
            viewModel.tourListPath(idCategory)
        }

        binding.tvCategory.text = when (idCategory) {
            1 -> "Nature"
            2 -> "Park"
            3 -> "All"
            else -> "Recommendation"

        }
    }

    // Recycler View Adapter
    private fun adapter() {
        binding.rvList.adapter =
            object : CoreListAdapter<ListItemBinding, Tour>(R.layout.list_item) {
                override fun onBindViewHolder(
                    holder: ItemViewHolder<ListItemBinding, Tour>,
                    position: Int
                ) {
                    val data = tour[position]
                    holder.binding.data = data

//                    holder.binding.ivFavourite.isVisible = data?.like == true

//                    val like = ?.like
//                    when (like){
//                        true -> holder.binding.ivFavourite.visibility
//                        else -> holder.binding.ivFavourite.isInvisible
//                    }

                    holder.binding.btnDetail.setOnClickListener {
                        openActivity<DetailActivity> {
                            putExtra(Cons.TOUR.TOUR, data)

                        }
                    }
                }
            }.initItem(tour)
    }

    //Function Observe
    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.tour.observe(this@ListActivity) {
                        binding.swipeLayout.isRefreshing = false
                        tour.clear()
                        tour.addAll(it)
                        binding.rvList.adapter?.notifyDataSetChanged()
                        Log.d("cel adapter", "list tour : $tour")
                        Log.d("cek adapter", "check it : $it")
                    }
                }
            }
        }

    }

    //Function Tour Nature
    private fun tourNature() {
        viewModel.tourListNature()
    }

    //Function Tour Park
    private fun tourPark() {
        viewModel.tourListPark()
    }

    //Function Tour All
    private fun tourAll() {
        viewModel.tourListAll()
    }

    //Function Tour Recommendation
    private fun tourRec() {
        viewModel.tourListRec()
    }

    private fun tourPath() {
//        viewModel.tourListPath()
    }

//    private fun looperData() {
//        tos("looperData")
//        Handler(Looper.getMainLooper()).postDelayed({
//            getData()
//        }, 3000)
//    }
//    override fun onPause() {
//        super.onPause()
//        getData()
//    }
//    override fun onResume() {
//        super.onResume()
//        tos("onResume")
//        getData()
//    }

}