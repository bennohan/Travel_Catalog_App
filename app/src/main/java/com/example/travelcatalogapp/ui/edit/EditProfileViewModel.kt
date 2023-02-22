package com.example.travelcatalogapp.ui.edit

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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val session: Session
) : BaseViewModel() {

    fun updateUser(name: String, phone: String, method: String) = viewModelScope.launch {
        ApiObserver({ apiService.userUpdate(method, name, phone) },
            false, object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val data = response.getJSONObject(ApiCode.DATA).toObject<User>(gson)
                    _apiResponse.send(ApiResponse().responseSuccess("Profile Updated"))
                    session.saveUser(data)

                }

                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                    _apiResponse.send(ApiResponse().responseError())

                }
            })
    }

    fun updateUserWithPhoto(name: String, phone: String, method: String, photo: File) =
        viewModelScope.launch {
            val fileBody = photo.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("image", photo.name, fileBody)
            ApiObserver({ apiService.userUpdateWithPhoto(method, name, phone, filePart) },
                false, object : ApiObserver.ResponseListener {
                    override suspend fun onSuccess(response: JSONObject) {
                        val data = response.getJSONObject(ApiCode.DATA).toObject<User>(gson)
                        _apiResponse.send(ApiResponse().responseSuccess("Profile Picture Updated"))
                        session.saveUser(data)
                    }

                    override suspend fun onError(response: ApiResponse) {
                        super.onError(response)
                        _apiResponse.send(ApiResponse().responseError())

                    }
                })
        }

}