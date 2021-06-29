package com.uni.information_security.ui.main

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import com.uni.information_security.R
import com.uni.information_security.base.BaseActivity
import com.uni.information_security.databinding.ActivityMainBinding
import com.uni.information_security.view_model.ViewModelFactory


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    val TAG = "MainActivity"

    companion object {
        fun getIntent(
            context: Context
        ): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }


    override fun getContentLayout(): Int {
        return R.layout.activity_main
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, ViewModelFactory(this)).get(MainViewModel::class.java)
    }

    override fun initView() {

    }

    override fun initListener() {

    }

    override fun observerLiveData() {
    }
}
