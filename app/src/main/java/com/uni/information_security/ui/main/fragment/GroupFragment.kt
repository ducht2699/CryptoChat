package com.uni.information_security.ui.main.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.uni.information_security.interfaces.IMainCallBack
import com.uni.information_security.R
import com.uni.information_security.base.BaseFragment
import com.uni.information_security.databinding.FragmentGroupBinding
import com.uni.information_security.ui.main.MainActivity
import com.uni.information_security.ui.main.MainViewModel

class GroupFragment : BaseFragment<MainViewModel, FragmentGroupBinding>() {

    companion object {
        fun getInstance(iMainCallBack: IMainCallBack) : Fragment {
            val fragment = GroupFragment()
            fragment.iMainCallBack = iMainCallBack
            return fragment
        }
    }

    private lateinit var iMainCallBack: IMainCallBack

    override fun getContentLayout(): Int {
        return R.layout.fragment_group
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun observerLiveData() {
    }

}