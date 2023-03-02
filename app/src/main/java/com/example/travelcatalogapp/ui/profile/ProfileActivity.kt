package com.example.travelcatalogapp.ui.profile

import android.os.Bundle
import com.crocodic.core.extension.openActivity
import com.example.travelcatalogapp.R
import com.example.travelcatalogapp.base.BaseActivity
import com.example.travelcatalogapp.data.Session
import com.example.travelcatalogapp.databinding.ActivityProfileBinding
import com.example.travelcatalogapp.ui.edit.EditProfileActivity
import com.example.travelcatalogapp.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity :
    BaseActivity<ActivityProfileBinding, ProfileViewModel>(R.layout.activity_profile) {

    @Inject
    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = session.getUser()
        if (user != null) {
            binding.user = user
        }


        //Button Edit Profile
        binding.btnEditProfile.setOnClickListener {
            openActivity<EditProfileActivity>()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            openActivity<LoginActivity>()
            finishAffinity()

        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}