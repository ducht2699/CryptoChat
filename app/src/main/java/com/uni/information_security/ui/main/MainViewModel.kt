package com.uni.information_security.ui.main

import android.annotation.SuppressLint
import com.uni.information_security.data.DataManager
import com.uni.information_security.network.Api
import com.uni.information_security.view_model.BaseViewModel
import javax.inject.Inject


@SuppressLint("CheckResult")
class MainViewModel(): BaseViewModel() {
    @Inject
    lateinit var dataManager: DataManager
    @Inject
    lateinit var api: Api

}