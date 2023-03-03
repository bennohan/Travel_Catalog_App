package com.example.travelcatalogapp.ui.list

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe()
        adapter()
        getData()

        //Swipe Refresh Layout
        binding.swipeLayout.setOnRefreshListener {
            getData()
            observe()
            adapter()
        }

        // Button Back
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

    }

    //Function GetData
    private fun getData() {
        when (intent.getIntExtra(Cons.CATEGORY.ID, 0)) {
            1 -> {
                //Receive Nature list from id 1
                tourNature()
                binding.tvCategory.text = "Nature"
            }
            2 -> {
                //Receive Park list from id 2
                tourPark()
                binding.tvCategory.text = "Park "

            }
            3 -> {
                //Receive All list from id 3
                tourAll()
                binding.tvCategory.text = "All"

            }
            4 -> {
                //Receive Recommendation list from id 1
                tourRec()
                binding.tvCategory.text = "Recommendation"
            }
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
}