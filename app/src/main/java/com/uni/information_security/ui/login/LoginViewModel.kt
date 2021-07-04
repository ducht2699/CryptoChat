package com.uni.information_security.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.uni.information_security.R
import com.uni.information_security.data.DataManager
import com.uni.information_security.model.request.chat.LoginRequest
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.network.Api
import com.uni.information_security.utils.*
import com.uni.information_security.view_model.BaseViewModel
import javax.inject.Inject


@SuppressLint("CheckResult")
class LoginViewModel() : BaseViewModel() {
    @Inject
    lateinit var dataManager: DataManager

    @Inject
    lateinit var api: Api

    val database = Firebase.database.reference

    val loginResponse = MutableLiveData<Boolean>()
    val initUserResponse = MutableLiveData<Boolean>()


    fun login(request: LoginRequest) {
        onRetrievePostListStart()
        usersList.clear()
        database.child("Users").get()
            .addOnCompleteListener { users ->
                for (sns in users.result?.children ?: ArrayList()) {
                    val user = sns.getValue<User>()
                    if (request.username == user?.username && request.password == user?.password) {
                        dataManager.save(PREF_USERNAME, user?.username)
                        dataManager.save(PREF_PASS, user?.password)
                        dataManager.save(PREF_KEY, user?.key)
                        dataManager.save(PREF_AUTO_LOGIN, true)
                        loginResponse.postValue(true)
                        onRetrievePostListFinish()
                        break
                    }
                }
                dataManager.save(PREF_AUTO_LOGIN, false)
                responseMessage.value = EMPTY_STRING
            }
            .addOnCanceledListener {
                onRetrievePostListFinish()
                errorMessage.postValue(R.string.str_error_on_get_data)
            }
    }


    fun autoLogin(context: Context) {
        if (dataManager.getBoolean(PREF_AUTO_LOGIN))
            login(
                LoginRequest(
                    dataManager.getString(PREF_USERNAME),
                    dataManager.getString(PREF_PASS)
                )
            )
        else {
            dataManager.save(PREF_AUTO_LOGIN, false)
            responseMessage.value = EMPTY_STRING
        }
    }

}