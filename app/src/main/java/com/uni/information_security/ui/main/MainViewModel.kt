package com.uni.information_security.ui.main

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.uni.information_security.R
import com.uni.information_security.data.DataManager
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.network.Api
import com.uni.information_security.ui.login.LoginViewModel
import com.uni.information_security.utils.*
import com.uni.information_security.view_model.BaseViewModel
import javax.inject.Inject


@SuppressLint("CheckResult")
class MainViewModel() : BaseViewModel() {
    @Inject
    lateinit var dataManager: DataManager

    private val database = FirebaseDatabase.getInstance().reference

    val userResponse = MutableLiveData<List<User?>>()
    private val userList = mutableListOf<User?>()


    fun getUsers() {
        onRetrievePostListStart()
        database.child(USER_PATH).get()
            .addOnCompleteListener { users ->
                userList.clear()
                for (sns in users.result?.children ?: ArrayList()) {
                    val user = sns.getValue<User>()
                    if (USER_DATA?.id != user?.id)
                        userList.add(user)
                }
                userResponse.postValue(userList)
                onRetrievePostListFinish()
            }
            .addOnCanceledListener {
                onRetrievePostListFinish()
            }
    }

}