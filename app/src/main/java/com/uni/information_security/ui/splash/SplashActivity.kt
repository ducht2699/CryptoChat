package com.uni.information_security.ui.splash

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.uni.information_security.R
import com.uni.information_security.base.BaseActivity
import com.uni.information_security.databinding.ActivitySplashBinding
import com.uni.information_security.ui.login.LoginActivity
import com.uni.information_security.ui.login.LoginViewModel
import com.uni.information_security.ui.main.MainActivity
import com.uni.information_security.utils.CommonUtils.showCustomUI
import com.uni.information_security.view_model.ViewModelFactory

class SplashActivity : BaseActivity<LoginViewModel, ActivitySplashBinding>() {

    override fun getContentLayout(): Int {
        return R.layout.activity_splash
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(LoginViewModel::class.java)
    }

    override fun initView() {
        showCustomUI()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initListener() {
        viewModel.autoLogin(this)
    }

    override fun observerLiveData() {
        viewModel.apply {
            loginResponse.observe(this@SplashActivity, {
                if (it) {
                    startActivity(MainActivity.getIntent(this@SplashActivity))
                    finishAffinity()
                } else {
                    startActivity(LoginActivity.getIntent(this@SplashActivity))
                    finishAffinity()
                }
            })
            responseMessage.observe(this@SplashActivity, {
                startActivity(LoginActivity.getIntent(this@SplashActivity))
                finishAffinity()
            })
        }
    }
}
