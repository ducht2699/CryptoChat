package com.uni.information_security.ui.main

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.uni.information_security.R
import com.uni.information_security.base.BaseActivity
import com.uni.information_security.databinding.ActivityMainBinding
import com.uni.information_security.interfaces.IMainCallBack
import com.uni.information_security.ui.main.fragment.GroupFragment
import com.uni.information_security.utils.CommonUtils.showCustomUI
import com.uni.information_security.view_model.ViewModelFactory


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(), IMainCallBack,
    View.OnClickListener {

    val TAG = "MainActivity"

    companion object {
        fun getIntent(
            context: Context
        ): Intent {
            return Intent(context, MainActivity::class.java)
        }
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
    }

    private fun replaceFragment(f: Fragment) {
        val fs = fm.beginTransaction()
        fs.replace(binding.flMain.id, f)
        fs.commit()
    }

    override fun initListener() {
        binding.toolbar.lnlRight.setOnClickListener(this)
    }

    override fun observerLiveData() {
    }

    override fun changeFragmentCallBack(isGroupFragment: Boolean) {
        if (isGroupFragment) {
            binding.toolbar.lnlBack.visibility = View.GONE
            binding.toolbar.cvUser.visibility = View.VISIBLE
            binding.toolbar.imvRight.visibility = View.GONE
        } else {
            binding.toolbar.lnlBack.visibility = View.VISIBLE
            binding.toolbar.cvUser.visibility = View.VISIBLE
            binding.toolbar.imvRight.visibility = View.VISIBLE
            binding.toolbar.imvRight.setImageResource(R.drawable.ic_info)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.lnl_right -> {
            }
        }
    }
}
