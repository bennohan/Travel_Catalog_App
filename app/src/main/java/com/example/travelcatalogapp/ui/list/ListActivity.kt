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


        //Ask About Holder
        binding.rvList.adapter =
            object : CoreListAdapter<ListItemBinding, Tour>(R.layout.list_item) {
                override fun onBindViewHolder(
                    holder: ItemViewHolder<ListItemBinding, Tour>,
                    position: Int
                ) {
                    val data = tour[position]
                    holder.binding.data = data

                    holder.binding.btnDetail.setOnClickListener {
                        openActivity<DetailActivity> {
                            putExtra(Cons.TOUR.TOUR,data)

                        }
                    }
                }
            }.initItem(tour)


        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        observe()

        val id = intent.getIntExtra(Cons.CATEGORY.ID, 0)
        when (id) {
            1 -> {
                viewModel.tourListNature()
                binding.tvCategory.text = "Nature"
            }
            2 -> {
                viewModel.tourListPark()
                binding.tvCategory.text = "Park "

            }
            3 -> {
                viewModel.tourListAll()
                binding.tvCategory.text = "All"

            }
            4 -> {
                viewModel.tourListRec()
                binding.tvCategory.text = "Recomendation"
            }
        }


        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getTourListAll() {
        viewModel.tourListAll()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.tour.observe(this@ListActivity) {
                        tour.addAll(it)
                        binding.rvList.adapter?.notifyDataSetChanged()
                        Log.d("cel adapter", "list tour : $tour")
                        Log.d("cek adapter", "check it : $it")

                    }
                }
            }
        }

    }

}