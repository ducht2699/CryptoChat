package com.uni.information_security.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.facebook.common.Common
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.uni.information_security.R
import com.uni.information_security.data.DataManager
import com.uni.information_security.model.request.chat.CreateAccountRequest
import com.uni.information_security.model.request.chat.LoginRequest
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.network.Api
import com.uni.information_security.utils.*
import com.uni.information_security.view_model.BaseViewModel
import javax.inject.Inject


@SuppressLint("CheckResult")
class LoginViewModel() : BaseViewModel() {
    companion object {
        const val USER_PATH = "Users"
    }

    @Inject
    lateinit var dataManager: DataManager

    @Inject
    lateinit var api: Api

    val database = Firebase.database.reference

    val loginResponse = MutableLiveData<Boolean>()
    val initUserResponse = MutableLiveData<Boolean>()


    @RequiresApi(Build.VERSION_CODES.O)
    fun login(request: LoginRequest) {
        onRetrievePostListStart()
        database.child(USER_PATH).get()
            .addOnCompleteListener { users ->
                for (sns in users.result?.children ?: ArrayList()) {
                    val user = sns.getValue<User>()
                    val password = CommonUtils.decrypt(
                        user?.password,
                        user?.id
                    )
                    if (request.username == user?.username && request.password == password) {
                        dataManager.save(PREF_USERNAME, user?.username)
                        dataManager.save(PREF_PASS, password)
                        dataManager.save(PREF_AUTO_LOGIN, true)
                        loginResponse.postValue(true)
                        onRetrievePostListFinish()
                        return@addOnCompleteListener
                    }
                }
                loginResponse.postValue(false)
                onRetrievePostListFinish()
                dataManager.save(PREF_AUTO_LOGIN, false)
                errorMessage.postValue(R.string.str_login_auth_error)
            }
            .addOnCanceledListener {
                onRetrievePostListFinish()
                errorMessage.postValue(R.string.str_error_on_get_data)
            }
    }


    @RequiresApi(Build.VERSION_CODES.O)
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

    val createAccountResponse = MutableLiveData<Boolean>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun createAccount(request: CreateAccountRequest) {
        onRetrievePostListStart()
        database.child(USER_PATH).get()
            .addOnCompleteListener { users ->
                var isExist = false
                for (sns in users.result?.children ?: ArrayList()) {
                    val user = sns.getValue<User>()
                    if (request.username == user?.username && request.password == CommonUtils.decrypt(
                            user?.password,
                            user?.username ?: EMPTY_STRING
                        )
                    ) {
                        isExist = true
                        break
                    } else isExist = false
                }

                if (!isExist) {
                    val id = database.push().key
                    val passEnc = CommonUtils.encrypt(
                        request.password ?: EMPTY_STRING,
                        id
                    )

                    database.child(USER_PATH).child(id!!)
                        .setValue(User(id, request.username, passEnc))
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                createAccountResponse.value = true
                                onRetrievePostListFinish()
                            }
                        }
                } else {
                    onRetrievePostListFinish()
                    errorMessage.value = R.string.str_create_auth_error
                }
            }
            .addOnCanceledListener {
                onRetrievePostListFinish()
                errorMessage.postValue(R.string.str_error_on_get_data)
            }
    }

}