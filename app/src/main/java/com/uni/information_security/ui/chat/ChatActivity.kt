package com.uni.information_security.ui.chat

import android.content.Intent
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.uni.information_security.R
import com.uni.information_security.base.BaseActivity
import com.uni.information_security.databinding.ActivityChatBinding
import com.uni.information_security.model.response.chat.Message
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.model.response.chat.UserInGroup
import com.uni.information_security.ui.chat_info.ChatInfoActivity
import com.uni.information_security.ui.main.MainActivity
import com.uni.information_security.utils.*
import com.uni.information_security.utils.CommonUtils.showCustomUI

class ChatActivity : BaseActivity<ChatViewModel, ActivityChatBinding>() {

    private var groupID: String? = ""
    private val messageList = mutableListOf<Message?>()
    private lateinit var messageAdapter: MessageAdapter
    private var isInGroup = true

    private val userInGroupList = mutableListOf<UserInGroup?>()
    private val userInfoList = mutableListOf<User?>()

    override fun getContentLayout(): Int {
        return R.layout.activity_chat
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
    }

    override fun initView() {
        showCustomUI()
        initToolbar()
    }

    private fun initToolbar() {
        binding.toolbar.tvTitleToolbar.text = GROUP_DATA?.name
        binding.toolbar.lnlBack.visibility = View.VISIBLE
        binding.toolbar.cvUser.visibility = View.VISIBLE
        CommonUtils.setImageFromBase64(GROUP_DATA?.avatar, binding.toolbar.imvAvatar, this)
        if (GROUP_DATA?.userList?.containsKey(USER_DATA?.id) == false) {
            isInGroup = false
            binding.lnlChat.visibility = View.GONE
            binding.toolbar.tvTitleToolbar.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.color_bg_search_edt
                )
            )
        }
        messageAdapter = MessageAdapter(messageList, isInGroup, userInfoList)
        binding.rcvMessages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcvMessages.adapter = messageAdapter
    }

    override fun initListener() {
        viewModel.getMessages()
        viewModel.getUsers()
        binding.toolbar.lnlBack.setOnClickListener {
            if (!isDoubleClick()) {
                startActivity(MainActivity.getIntent(this))
                finishAffinity()
            }
        }
        binding.imvSend.setOnClickListener {
            if (!isDoubleClick()) {
                val message = binding.edtChat.text.toString().trim()
                if (message != EMPTY_STRING) {
                    viewModel.sendMessage(message)
                }
            }
        }
        binding.toolbar.lnlRight.setOnClickListener {
            if (!isDoubleClick()) {
                for (user in userInGroupList) {
                    if (user?.id == USER_DATA?.id) {
                        val intent = Intent(this, ChatInfoActivity::class.java)
                        intent.putExtra(EXTRA_IS_OWNER, user?.owner)
                        intent.putExtra(EXTRA_IS_OUTER, false)
                        startActivity(intent)
                        finishAffinity()
                        return@setOnClickListener
                    }
                }
                val intent = Intent(this, ChatInfoActivity::class.java)
                intent.putExtra(EXTRA_IS_OWNER, false)
                intent.putExtra(EXTRA_IS_OUTER, true)
                startActivity(intent)
                finishAffinity()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun observerLiveData() {
        viewModel.apply {
            messageSendResponse.observe(this@ChatActivity, { result ->
                if (result) {
                    binding.edtChat.text.clear()
                }
            })
            messageAddResponse.observe(this@ChatActivity, { message ->
                if (!isInGroup)
                    messageAdapter.addItem(
                        Message(
                            message?.idMessage,
                            message?.idUser,
                            message?.content
                        )
                    )
                else {
                    val decMessage = CommonUtils.decrypt(message?.content, message?.idMessage)
                    messageAdapter.addItem(Message(message?.idMessage, message?.idUser, decMessage))
                }
            })
            userInfoResponse.observe(this@ChatActivity, { data ->
                userInfoList.clear()
                userInfoList.addAll(data)
                getUserInGroup()
            })
            userInGroupResponse.observe(this@ChatActivity, {data->
                userInGroupList.clear()
                userInGroupList.addAll(data)
            })
        }
    }

    override fun onBackPressed() {
        startActivity(MainActivity.getIntent(this))
        finishAffinity()
    }
}