package com.uni.information_security.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.uni.information_security.R
import com.uni.information_security.base.BaseActivity
import com.uni.information_security.databinding.ActivityChatBinding
import com.uni.information_security.ui.main.MainActivity
import com.uni.information_security.utils.CommonUtils.showCustomUI
import com.uni.information_security.utils.EXTRA_GROUP_ID

class ChatActivity : BaseActivity<ChatViewModel, ActivityChatBinding>() {

    private var groupID : String? = ""

    override fun getContentLayout(): Int {
        return R.layout.activity_chat
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
    }

    override fun initView() {
        showCustomUI()
        groupID = intent.extras?.getString(EXTRA_GROUP_ID, "")

    }

    override fun initListener() {

    }

    override fun observerLiveData() {

    }

    override fun onBackPressed() {
        startActivity(MainActivity.getIntent(this))
        finishAffinity()
    }
}