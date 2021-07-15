package com.uni.information_security.ui.create_group

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.uni.information_security.R
import com.uni.information_security.base.BaseActivity
import com.uni.information_security.databinding.ActivityCreateGroupBinding
import com.uni.information_security.model.response.chat.Group
import com.uni.information_security.model.response.chat.Message
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.model.response.chat.UserInGroup
import com.uni.information_security.ui.main.MainActivity
import com.uni.information_security.utils.CommonUtils
import com.uni.information_security.utils.CommonUtils.showCustomUI
import com.uni.information_security.utils.EMPTY_STRING
import com.uni.information_security.utils.USER_DATA

class CreateGroupActivity : BaseActivity<CreateGroupViewModel, ActivityCreateGroupBinding>(),
    UserCreateRoomAdapter.IOnUserClicked {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, CreateGroupActivity::class.java)
        }

        const val DELAY_DURATION = 1000L
    }

    private val avatar =
        "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"

    private val userList = mutableListOf<User?>()
    private val userSelectedList = mutableListOf<User?>()
    private val userAdapter = UserCreateRoomAdapter(userList, this, ArrayList(), true)

    override fun getContentLayout(): Int {
        return R.layout.activity_create_group
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(CreateGroupViewModel::class.java)
    }

    override fun initView() {
        showCustomUI()
        initToolbar()
        initUsers()
        binding.edtGroupName.setText(
            resources.getString(
                R.string.str_group_chat,
                USER_DATA?.username
            )
        )
    }

    private fun initToolbar() {
        binding.toolbar.lnlBack.visibility = View.VISIBLE
        binding.toolbar.cvUser.visibility = View.INVISIBLE
        binding.toolbar.tvTitleToolbar.text = resources.getString(R.string.str_create_room)
        binding.toolbar.lnlRight.visibility = View.INVISIBLE
    }

    private fun initUsers() {
        binding.rcvUsers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvUsers.adapter = userAdapter
    }

    override fun initListener() {
        viewModel.getUsers()
        binding.toolbar.lnlBack.setOnClickListener {
            if (!isDoubleClick()) {
                finishAffinity()
            }
        }
        binding.btnCreate.setOnClickListener {
            if (!isDoubleClick()) {
                val groupName = binding.edtGroupName.text.toString().trim()
                if (groupName == EMPTY_STRING) {
                    Toast.makeText(this, "Tên nhóm không được để trống", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val userInGroupList = mutableMapOf<String?, UserInGroup?>()
                userInGroupList[USER_DATA?.id] = UserInGroup(USER_DATA?.id, true)
                for (user in userSelectedList) {
                    userInGroupList[user?.id] = UserInGroup(user?.id, false)
                }
                viewModel.createGroup(
                    groupName,
                    avatar,
                    userInGroupList
                )
            }
        }
        binding.edtSearchUser.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun observerLiveData() {
        viewModel.apply {
            createGroupResponse.observe(this@CreateGroupActivity, { result ->
                if (result) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        Toast.makeText(
                            this@CreateGroupActivity,
                            resources.getString(R.string.str_success),
                            Toast.LENGTH_LONG
                        ).show()

                    }, DELAY_DURATION)
                    startActivity(MainActivity.getIntent(this@CreateGroupActivity))
                    finishAffinity()
                } else {
                    Toast.makeText(
                        this@CreateGroupActivity,
                        resources.getString(R.string.str_error),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
            //user
            userAddResponse.observe(this@CreateGroupActivity, { data ->
                userList.clear()
                userList.addAll(data)
                userAdapter.notifyItemRangeInserted(0, userList.size)
            })
            userChangeResponse.observe(this@CreateGroupActivity, { data ->
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
            userRemovedResponse.observe(this@CreateGroupActivity, { data ->
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
        }
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
        //group text
        binding.edtGroupName.text.clear()
        if (userSelectedList.size == 0) {
            binding.edtGroupName.setText(
                resources.getString(
                    R.string.str_group_chat,
                    USER_DATA?.username
                )
            )
        } else
            for (user in userSelectedList) {
                binding.edtGroupName.text.append("${user?.username}")
                if (userSelectedList.indexOf(user) < userSelectedList.size - 1)
                    binding.edtGroupName.text.append(", ")
            }
    }

    override fun onBackPressed() {
        startActivity(MainActivity.getIntent(this@CreateGroupActivity))
        finishAffinity()
    }
}