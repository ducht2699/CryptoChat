package com.uni.information_security.ui.chat_info

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.uni.information_security.data.DataManager
import com.uni.information_security.model.response.chat.Group
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.model.response.chat.UserInGroup
import com.uni.information_security.utils.*
import com.uni.information_security.view_model.BaseViewModel
import javax.inject.Inject

class ChatInfoViewModel : BaseViewModel() {

    @Inject
    lateinit var dataManager: DataManager

    private val database = FirebaseDatabase.getInstance().reference

    val userAddResponse = MutableLiveData<List<User?>>()
    val userChangeResponse = MutableLiveData<User?>()
    val userRemovedResponse = MutableLiveData<User?>()
    private val userList = mutableListOf<User?>()
    fun getUsers() {
        onRetrievePostListStart()
        database.child(USER_PATH).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue<User>()

                userList.add(user)
                userAddResponse.postValue(userList)
                onRetrievePostListFinish()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue<User>()
                if (user?.id != USER_DATA?.id)
                    userChangeResponse.postValue(user)
                onRetrievePostListFinish()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                if (user?.id != USER_DATA?.id)
                    userRemovedResponse.postValue(user)
                onRetrievePostListFinish()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                onRetrievePostListFinish()
            }

            override fun onCancelled(error: DatabaseError) {
                onRetrievePostListFinish()
            }

        })
    }

    private val userInGroup = mutableListOf<UserInGroup?>()
    val userInGroupResponse = MutableLiveData<List<UserInGroup?>>()
    fun getUserInGroup() {
        onRetrievePostListStart()
        database.child(GROUP_PATH).child(GROUP_DATA?.id!!).child(USER_IN_GROUP_PATH).get()
            .addOnCompleteListener {
                for (sns in it.result?.children ?: ArrayList()) {
                    val user = sns.getValue<UserInGroup>()
                    userInGroup.add(user)
                }
                userInGroupResponse.value = userInGroup
            }
    }


    val saveGroupResponse = MutableLiveData<Boolean>()
    fun saveGroup(
        userList: MutableMap<String?, UserInGroup?>
    ) {
        onRetrievePostListStart()
        database.child(GROUP_PATH).child(GROUP_DATA?.id!!).child(USER_IN_GROUP_PATH).setValue(userList).addOnSuccessListener {
            saveGroupResponse.postValue(true)
            onRetrievePostListFinish()
        }
            .addOnCanceledListener {
                saveGroupResponse.postValue(false)
                onRetrievePostListFinish()
            }
    }


    val leaveGroupResponse = MutableLiveData<Boolean>()
    fun leave(userInGroup: List<UserInGroup?>) {
        val userMap = mutableMapOf<String?, UserInGroup?>()
        for (user in userInGroup) {
            userMap[user?.id] = user
        }
        database.child(GROUP_PATH).child(GROUP_DATA?.id!!).child(USER_IN_GROUP_PATH)
            .setValue(userMap).addOnCompleteListener {
            leaveGroupResponse.value = true
        }
    }
}