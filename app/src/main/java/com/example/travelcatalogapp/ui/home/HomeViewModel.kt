package com.example.travelcatalogapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.extension.toList
import com.example.travelcatalogapp.api.ApiService
import com.example.travelcatalogapp.base.BaseViewModel
import com.example.travelcatalogapp.data.ImageSlide
import com.example.travelcatalogapp.data.Session
import com.example.travelcatalogapp.data.Tour
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val session: Session
) : BaseViewModel() {

    var tour = MutableLiveData<List<Tour>>()
    var image = MutableLiveData<List<ImageSlide>>()

    //tour list Function
    fun tourList() = viewModelScope.launch {
        ApiObserver({ apiService.tourList() }, false, object : ApiObserver.ResponseListener {
            override suspend fun onSuccess(response: JSONObject) {
                val status = response.getInt(ApiCode.STATUS)
                if (status == ApiCode.SUCCESS) {

                    val data = response.getJSONArray(ApiCode.DATA).toList<Tour>(gson)
                    tour.postValue(data)

                } else {
                    val message = response.getString(ApiCode.MESSAGE)
                }
            }
        })
    }

    //Function Image Slider
    fun imageSlider() = viewModelScope.launch {
        ApiObserver({ apiService.imageSlider() }, false, object : ApiObserver.ResponseListener {
            override suspend fun onSuccess(response: JSONObject) {
                val status = response.getInt(ApiCode.STATUS)
                if (status == ApiCode.SUCCESS) {

                    val dataImage = response.getJSONArray(ApiCode.DATA).toList<ImageSlide>(gson)

                    image.postValue(dataImage)

                } else {
                    val message = response.getString(ApiCode.MESSAGE)
                }
            }
        })
    }

}