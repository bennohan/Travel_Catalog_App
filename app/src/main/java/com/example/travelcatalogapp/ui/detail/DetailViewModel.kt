package com.example.travelcatalogapp.ui.detail

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.extension.toObject
import com.example.travelcatalogapp.api.ApiService
import com.example.travelcatalogapp.base.BaseViewModel
import com.example.travelcatalogapp.data.Session
import com.example.travelcatalogapp.data.User
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val session: Session
) : BaseViewModel() {

    //Favourite
    fun favouriteTour(tourId : Int) = viewModelScope.launch {
        ApiObserver({ apiService.postFavourite(tourId) },
            false, object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val data = response.getJSONObject(ApiCode.DATA).toObject<User>(gson)
                    _apiResponse.send(ApiResponse().responseSuccess("Favourite Added"))
                    session.saveUser(data)

                }

                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                    _apiResponse.send(ApiResponse().responseError())

                }
            })
    }


}