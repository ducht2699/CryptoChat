package com.uni.information_security.ui.personal

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.uni.information_security.data.DataManager
import com.uni.information_security.model.response.chat.User
import com.uni.information_security.utils.*
import com.uni.information_security.view_model.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PersonalViewModel : BaseViewModel() {
    @Inject
    lateinit var dataManager: DataManager

    private val database = FirebaseDatabase.getInstance().reference

    var createImageResponse: MutableLiveData<Bitmap> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun createImage(
        activity: Activity,
        data: List<com.esafirm.imagepicker.model.Image>
    ) {
        var isRun = true
        var isFinish = true
        var bitmap: Bitmap? = null
        Observable.fromIterable(data)
            .map {
                if (isRun) {
                    isRun = false
                    for (i: Int in data.indices) {
                        val file = CommonUtils.createRotatedFile(data[i].path, activity)
                        bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrievePostListStart() }
            .doOnTerminate { onRetrievePostListFinish() }
            .subscribe {
                if (isFinish) {
                    isFinish = false
                    createImageResponse.postValue(bitmap)
                }
            }
    }

    val setAvatarResult = MutableLiveData<String?>()
    fun setAvatar(avatar: String?) {
        onRetrievePostListStart()
        USER_DATA = User(USER_DATA?.id, USER_DATA?.username, USER_DATA?.password, avatar)
        database.child(USER_PATH).child(USER_DATA?.id ?: EMPTY_STRING).setValue(USER_DATA)
            .addOnCompleteListener {
                setAvatarResult.postValue(avatar)
                onRetrievePostListFinish()
            }.addOnCanceledListener { onRetrievePostListFinish() }
    }

    fun logOut() {
        dataManager.save(PREF_AUTO_LOGIN, false)
    }

    val changePasswordResponse = MutableLiveData<String>()
    fun changePassword(newPassEnc: String) {
        onRetrievePostListStart()
        val tempUser = User(USER_DATA?.id, USER_DATA?.username, newPassEnc, USER_DATA?.avatar)
        database.child(USER_PATH).child(USER_DATA?.id ?: EMPTY_STRING).setValue(tempUser)
            .addOnCompleteListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dataManager.save(PREF_PASS, CommonUtils.decrypt(newPassEnc, USER_DATA?.id))
                }
                changePasswordResponse.postValue(newPassEnc)
                onRetrievePostListFinish()
            }
            .addOnCanceledListener {
                onRetrievePostListFinish()
            }
    }
}