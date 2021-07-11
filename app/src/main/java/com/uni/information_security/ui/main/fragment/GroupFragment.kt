package com.uni.information_security.ui.main.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.uni.information_security.interfaces.IMainCallBack
import com.uni.information_security.R
import com.uni.information_security.base.BaseFragment
import com.uni.information_security.databinding.FragmentGroupBinding
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.ui.main.MainActivity
import com.uni.information_security.ui.main.MainViewModel

class GroupFragment : BaseFragment<MainViewModel, FragmentGroupBinding>(),
    UserAdapter.IUserCallBack {

    companion object {
        fun getInstance(iMainCallBack: IMainCallBack): Fragment {
            val fragment = GroupFragment()
            fragment.iMainCallBack = iMainCallBack
            return fragment
        }
    }

    private val userList = mutableListOf<User?>()
    private val userAdapter = UserAdapter(userList, this)

    private lateinit var iMainCallBack: IMainCallBack

    override fun getContentLayout(): Int {
        return R.layout.fragment_group
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }


    override fun initView() {
        initUserRcv()
        initGroupRcv()
        getUsers()
    }

    private fun getUsers() {
        viewModel.getUsers()
    }

    private fun initUserRcv() {
        binding.rcvUsers.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvUsers.adapter = userAdapter
    }

    private fun initGroupRcv() {
        binding.rcvGroups.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun initListener() {
    }

    override fun observerLiveData() {
        viewModel.apply {
            userResponse.observe(this@GroupFragment, { data ->
                userList.clear()
                userList.addAll(data)
                userAdapter.notifyItemInserted(userList.size - 1)
            })
        }
    }

    override fun onUserClick(user: User?) {
        Toast.makeText(context, user?.username, Toast.LENGTH_SHORT).show()
    }

}