package com.uni.information_security.ui.chat_info

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.uni.information_security.R
import com.uni.information_security.base.BaseActivity
import com.uni.information_security.databinding.ActivityChatInfoBinding
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.model.response.chat.UserInGroup
import com.uni.information_security.ui.chat.ChatActivity
import com.uni.information_security.ui.create_group.UserCreateRoomAdapter
import com.uni.information_security.ui.main.MainActivity
import com.uni.information_security.utils.*
import com.uni.information_security.utils.CommonUtils.showCustomUI

class ChatInfoActivity : BaseActivity<ChatInfoViewModel, ActivityChatInfoBinding>(),
    UserCreateRoomAdapter.IOnUserClicked {

    private val userList = mutableListOf<User?>()
    private val userSelectedList = mutableListOf<User?>()
    private lateinit var userAdapter: UserCreateRoomAdapter


    private val userInGroupList = mutableListOf<UserInGroup?>()
    override fun getContentLayout(): Int {
        return R.layout.activity_chat_info
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChatInfoViewModel::class.java)
    }

    override fun initView() {
        showCustomUI()
        initUsers()
        val isOwner = intent?.extras?.getBoolean(EXTRA_IS_OWNER)
        if (isOwner == false) {
            binding.rcvUsers.isEnabled = false
            binding.btnSave.visibility = View.INVISIBLE
        }
        val isOuter = intent?.extras?.getBoolean(EXTRA_IS_OUTER)
        if (isOuter == true) {
            binding.btnLeave.visibility = View.GONE
        }
    }


    private fun initUsers() {
        userAdapter = UserCreateRoomAdapter(
            userList, this, userInGroupList, intent?.extras?.getBoolean(
                EXTRA_IS_OWNER
            )
        )
        binding.rcvUsers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvUsers.adapter = userAdapter
        viewModel.getUserInGroup()
    }

    override fun initListener() {
        binding.btnLeave.setOnClickListener {

            for (user in userInGroupList) {
                if (user?.id == USER_DATA?.id) {
                    userInGroupList.remove(user)
                    break
                }
            }
            for (user in userInGroupList) {
                if (user?.id != USER_DATA?.id) {
                    user?.owner = true
                    break
                }
            }
            viewModel.leave(userInGroupList)
        }
        binding.btnSave.setOnClickListener {
            if (!isDoubleClick()) {

                val userInGroupList = mutableMapOf<String?, UserInGroup?>()
                userInGroupList[USER_DATA?.id] = UserInGroup(USER_DATA?.id, true)
                for (user in userSelectedList) {
                    var check = user?.id == USER_DATA?.id
                    userInGroupList[user?.id] = UserInGroup(user?.id, check)
                }
                viewModel.saveGroup(
                    userInGroupList
                )
            }
        }
    }

    override fun observerLiveData() {
        viewModel.apply {
            //user
            userAddResponse.observe(this@ChatInfoActivity, { data ->
                userList.clear()
                userList.addAll(data)
                userAdapter.notifyItemRangeInserted(0, userList.size)
            })
            userChangeResponse.observe(this@ChatInfoActivity, { data ->
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
            userRemovedResponse.observe(this@ChatInfoActivity, { data ->
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
            userInGroupResponse.observe(this@ChatInfoActivity, { data ->
                userInGroupList.clear()
                userInGroupList.addAll(data)
                getUsers()
            })

            saveGroupResponse.observe(this@ChatInfoActivity, { data ->
                if (data) {
                    startActivity(Intent(this@ChatInfoActivity, ChatActivity::class.java))
                    finishAffinity()
                } else {
                    Toast.makeText(
                        this@ChatInfoActivity,
                        resources.getString(R.string.str_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            leaveGroupResponse.observe(this@ChatInfoActivity, { data ->
                startActivity(Intent(MainActivity.getIntent(this@ChatInfoActivity)))
                finishAffinity()
            })
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, ChatActivity::class.java))
        finishAffinity()
    }

    override fun onUserClicked(data: User?, isChecked: Boolean) {
        if (isChecked) {
            userSelectedList.add(data)
        } else {
            if (userSelectedList.contains(data)) {
                userSelectedList.remove(data)

            } else {
                Toast.makeText(this, resources.getString(R.string.str_error), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}