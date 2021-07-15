package com.uni.information_security.ui.main

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.uni.information_security.R
import com.uni.information_security.base.BaseActivity
import com.uni.information_security.databinding.ActivityMainBinding
import com.uni.information_security.interfaces.IMainCallBack
import com.uni.information_security.ui.chat.ChatActivity
import com.uni.information_security.ui.create_group.CreateGroupActivity
import com.uni.information_security.ui.login.LoginActivity
import com.uni.information_security.ui.main.fragment.GroupFragment
import com.uni.information_security.ui.personal.PersonalActivity
import com.uni.information_security.utils.CommonUtils
import com.uni.information_security.utils.CommonUtils.showCustomUI
import com.uni.information_security.utils.EXTRA_GROUP_ID
import com.uni.information_security.utils.USER_DATA
import com.uni.information_security.view_model.ViewModelFactory


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), IMainCallBack,
    View.OnClickListener {

    companion object {
        fun getIntent(
            context: Context
        ): Intent {
            return Intent(context, MainActivity::class.java)
        }
        const val CODE_START_PERSONAL = 123
    }

    private lateinit var fm: FragmentManager

    override fun getContentLayout(): Int {
        return R.layout.activity_main
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, ViewModelFactory(this)).get(MainViewModel::class.java)
    }

    override fun initView() {
        showCustomUI()
        binding.toolbar.tvTitleToolbar.text = resources.getString(R.string.app_name)
        fm = supportFragmentManager
        replaceFragment(GroupFragment.getInstance(this))
        //avatar
        CommonUtils.setImageFromBase64(USER_DATA?.avatar, binding.toolbar.imvAvatar, this)
    }

    private fun replaceFragment(f: Fragment) {
        val fs = fm.beginTransaction()
        fs.replace(binding.flMain.id, f)
        fs.commit()
    }

    override fun initListener() {
        binding.toolbar.lnlRight.setOnClickListener(this)
        binding.toolbar.imvAvatar.setOnClickListener(this)
    }

    override fun observerLiveData() {
    }

    override fun changeFragmentCallBack(isGroupFragment: Boolean, data: String?) {
        if (isGroupFragment) {
            binding.toolbar.lnlBack.visibility = View.GONE
            binding.toolbar.cvUser.visibility = View.VISIBLE
            binding.toolbar.imvRight.visibility = View.GONE
        } else {
            val i = Intent(binding.root.context, ChatActivity::class.java)
            i.putExtra(EXTRA_GROUP_ID, data)
            startActivity(i)
            finishAffinity()
        }
    }

    override fun userUnavailable() {
        startActivity(LoginActivity.getIntent(this))
        finishAffinity()
    }

    override fun updateUIUser() {
        CommonUtils.setImageFromBase64(USER_DATA?.avatar, binding.toolbar.imvAvatar, this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imv_avatar -> {
                if (!isDoubleClick()) {
                    startActivityForResult(Intent(this, PersonalActivity::class.java), CODE_START_PERSONAL)
                }
            }
            R.id.lnl_right -> {
                if (!isDoubleClick()) {
                    startActivity(CreateGroupActivity.getIntent(this))
                    finishAffinity()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_START_PERSONAL && resultCode == RESULT_OK) {
            CommonUtils.setImageFromBase64(USER_DATA?.avatar, binding.toolbar.imvAvatar, this)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
