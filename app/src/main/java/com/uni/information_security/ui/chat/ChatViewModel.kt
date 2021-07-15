package com.uni.information_security.ui.chat

import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.uni.information_security.model.response.chat.Message
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.model.response.chat.UserInGroup
import com.uni.information_security.utils.*
import com.uni.information_security.view_model.BaseViewModel

class ChatViewModel : BaseViewModel() {
    val database = FirebaseDatabase.getInstance().reference

    val messageList = mutableListOf<Message?>()
    val messageAddResponse = MutableLiveData<Message?>()

    fun getMessages() {
        onRetrievePostListStart()
        database.child(GROUP_PATH).child(GROUP_DATA?.id!!).child(MESSAGE_PATH)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue<Message>()
                    messageAddResponse.value = message
                    onRetrievePostListFinish()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        onRetrievePostListFinish()
    }

    val messageSendResponse = MutableLiveData<Boolean>()
    fun sendMessage(message: String) {
        val messageID = database.push().key
        var messageEnc: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            messageEnc = CommonUtils.encrypt(message, messageID)
        }
        val messageItem = Message(messageID, USER_DATA?.id, messageEnc)
        database.child(GROUP_PATH).child(GROUP_DATA?.id!!).child(MESSAGE_PATH).child(messageID!!)
            .setValue(messageItem)
            .addOnSuccessListener {
                messageSendResponse.value = true
            }
            .addOnCanceledListener { messageSendResponse.value = false }
    }

    private val userInfoList = mutableListOf<User?>()
    val userInfoResponse = MutableLiveData<List<User?>>()

    private val userInGroupList = mutableListOf<UserInGroup?>()
    val userInGroupResponse = MutableLiveData<List<UserInGroup?>>()
    fun getUsers() {
        onRetrievePostListStart()
        database.child(USER_PATH).get().addOnCompleteListener {
            for (sns in it.result?.children ?: ArrayList()) {
                val user = sns.getValue<User>()
                userInfoList.add(user)
            }
            userInfoResponse.value = userInfoList
        }
    }
    fun getUserInGroup() {
        onRetrievePostListStart()
        database.child(GROUP_PATH).child(GROUP_DATA?.id!!).child(USER_IN_GROUP_PATH).get().addOnCompleteListener {
            for (sns in it.result?.children ?: ArrayList()) {
                val user = sns.getValue<UserInGroup>()
                userInGroupList.add(user)
            }
            userInGroupResponse.value = userInGroupList
        }
    }


}