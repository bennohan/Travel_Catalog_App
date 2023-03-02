package com.example.travelcatalogapp.ui.login

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.isEmptyRequired
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.textOf
import com.crocodic.core.extension.tos
import com.example.travelcatalogapp.R
import com.example.travelcatalogapp.base.BaseActivity
import com.example.travelcatalogapp.databinding.ActivityLoginBinding
import com.example.travelcatalogapp.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnLogin.setOnClickListener {
            if (binding.etPhone.isEmptyRequired(R.string.mustFill) || binding.etPassword.isEmptyRequired(R.string.mustFill)
            ) {
                return@setOnClickListener
            }
            val phone = binding.etPhone.textOf()
            val password = binding.etPassword.textOf()
            viewModel.login(phone, password)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> loadingDialog.show("login..in")
                            ApiStatus.SUCCESS -> {
                                tos(it.message ?: "Login Success")
                                loadingDialog.dismiss()
                                openActivity<HomeActivity>()
                                finish()
                            }
                            ApiStatus.ERROR -> {
                                loadingDialog.dismiss()
                                tos(it.message ?: "login Failed")
                            }
                            else -> loadingDialog.setResponse("Else")
                        }
                    }
                }
            }
        }
    }
}