package com.uni.information_security.ui.main.fragment

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.uni.information_security.interfaces.IMainCallBack
import com.uni.information_security.R
import com.uni.information_security.base.BaseFragment
import com.uni.information_security.databinding.FragmentGroupBinding
import com.uni.information_security.model.response.chat.Group
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.ui.login.LoginActivity
import com.uni.information_security.ui.login.LoginViewModel
import com.uni.information_security.ui.main.MainActivity
import com.uni.information_security.ui.main.MainViewModel
import com.uni.information_security.utils.USER_DATA
import com.uni.information_security.utils.USER_PATH

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

    private val groupList = mutableListOf<Group?>()
    private val groupAdapter = GroupAdapter(groupList)

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
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rcvGroups.adapter = groupAdapter
    }

    override fun initListener() {
        viewModel.getGroups()
    }

    override fun observerLiveData() {
        viewModel.apply {
            userResponse.observe(this@GroupFragment, { data ->
                userList.clear()
                userList.addAll(data)
                userAdapter.notifyItemRangeInserted(0, userList.size)
            })
            userChangeResponse.observe(this@GroupFragment, { data ->
                if (data?.id == USER_DATA?.id) {
                    USER_DATA = data
                    iMainCallBack.updateUIUser()
                }
                var pos = -1
                for (user in userList) {
                    if (data?.id == user?.id) {
                        pos = userList.indexOf(user)
                        break
                    }
                }
                if (pos != -1) {
                    userList[pos] = data
                    userAdapter.notifyItemChanged(pos)
                }

            })
            userRemovedResponse.observe(this@GroupFragment, { data ->
                if (data?.id == USER_DATA?.id) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(
                            binding.root.context,
                            binding.root.context.resources.getString(R.string.str_account_unavailable),
                            Toast.LENGTH_LONG
                        ).show()
                    }, 1000)
                    iMainCallBack.userUnavailable()
                }
                var pos = -1
                for (user in userList) {
                    if (data?.id == user?.id) {
                        pos = userList.indexOf(user)
                        break
                    }
                }
                if (pos != -1) {
                    userAdapter.removeItem(pos)
                }
            })
            groupsResponse.observe(this@GroupFragment, { data ->
                groupList.clear()
                groupList.addAll(data)
                groupAdapter.notifyItemRangeInserted(0, groupList.size)
            })
        }
    }

    override fun onUserClick(user: User?) {
        Toast.makeText(context, user?.username, Toast.LENGTH_SHORT).show()
    }

}