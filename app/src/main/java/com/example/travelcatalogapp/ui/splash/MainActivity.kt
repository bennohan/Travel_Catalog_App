package com.example.travelcatalogapp.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.crocodic.core.extension.openActivity
import com.example.travelcatalogapp.R
import com.example.travelcatalogapp.base.BaseActivity
import com.example.travelcatalogapp.data.Session
import com.example.travelcatalogapp.data.User
import com.example.travelcatalogapp.databinding.ActivityMainBinding
import com.example.travelcatalogapp.ui.home.HomeActivity
import com.example.travelcatalogapp.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :BaseActivity<ActivityMainBinding , MainViewModel>(R.layout.activity_main) {

    @Inject
    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            val isUser = session.getUser()
            if (isUser == null){
                openActivity<LoginActivity>()
            }else{
                openActivity<HomeActivity>()
            }
        },4000)
    }
}