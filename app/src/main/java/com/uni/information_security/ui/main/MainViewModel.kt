package com.uni.information_security.ui.main

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.uni.information_security.R
import com.uni.information_security.data.DataManager
import com.uni.information_security.model.response.chat.Group
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.utils.*
import com.uni.information_security.view_model.BaseViewModel
import javax.inject.Inject


@SuppressLint("CheckResult")
class MainViewModel() : BaseViewModel() {
    @Inject
    lateinit var dataManager: DataManager

    private val database = FirebaseDatabase.getInstance().reference

    val userResponse = MutableLiveData<List<User?>>()
    val userChangeResponse = MutableLiveData<User?>()
    val userRemovedResponse = MutableLiveData<User?>()
    private val userList = mutableListOf<User?>()

    private val groupList = mutableListOf<Group?>()
    val groupsResponse = MutableLiveData<List<Group?>>()

    fun getGroups() {
        onRetrievePostListStart()
        database.child(GROUP_PATH).get()
            .addOnCompleteListener { users ->
                groupList.clear()
                for (sns in users.result?.children ?: ArrayList()) {
                    val group = sns.getValue<Group>()
                    groupList.add(group)
                }
                groupsResponse.postValue(groupList)
                onRetrievePostListFinish()
            }
            .addOnCanceledListener {
                onRetrievePostListFinish()
            }
    }

    fun getUsers() {
        onRetrievePostListStart()
        database.child(USER_PATH).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue<User>()
                if (user?.id != USER_DATA?.id) {
                    userList.add(user)
                    userResponse.postValue(userList)
                    onRetrievePostListFinish()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue<User>()
                dataManager.save(PREF_USERNAME, user?.username)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dataManager.save(PREF_PASS, CommonUtils.decrypt(user?.password, user?.id))
                }
                userChangeResponse.postValue(user)
                onRetrievePostListFinish()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()
                if (user?.id == USER_DATA?.id) {
                    dataManager.save(PREF_AUTO_LOGIN, false)
                }
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

}