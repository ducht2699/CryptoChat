package com.uni.information_security.view_model

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uni.information_security.R
import com.uni.information_security.injection.DaggerViewModelInjector
import com.uni.information_security.injection.NetworkModule
import com.uni.information_security.injection.ViewModelInjector
import com.uni.information_security.model.response.Message
import com.uni.information_security.ui.create_group.CreateGroupViewModel
import com.uni.information_security.ui.login.LoginViewModel
import com.uni.information_security.ui.main.MainViewModel
import com.uni.information_security.ui.personal.PersonalViewModel
import com.uni.information_security.utils.GsonUtils
import com.uni.information_security.utils.myapp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.net.ssl.HttpsURLConnection

abstract class BaseViewModel : ViewModel() {

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val responseMessage: MutableLiveData<String?> = MutableLiveData()

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .applicationComponent(myapp!!)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is LoginViewModel -> injector.inject(this)
            is MainViewModel -> injector.inject(this)
            is PersonalViewModel -> injector.inject(this)
            is CreateGroupViewModel -> injector.inject(this)
        }
    }

    protected fun onRetrievePostListStart() {
        isLoading.value = true
        errorMessage.value = null
    }

    protected fun onRetrievePostListFinish() {
        Handler(Looper.getMainLooper()).postDelayed({
            isLoading.value = false
        }, 1000)
    }

    protected fun handleApiError(error: Throwable?) {
        if (error == null) {
            errorMessage.value = R.string.api_default_error
            return
        }

        if (error is HttpException) {
            Log.w("ERROR", error.message() + error.code())
            when (error.code()) {
                HttpURLConnection.HTTP_BAD_REQUEST -> try {
                    val message: Message = GsonUtils.deserialize(
                        error.response()?.errorBody()?.string(),
                        Message::class.java
                    )
                    responseMessage.value = message.message
                } catch (e: IOException) {
                    e.printStackTrace()
                    responseMessage.value = error.message
                }
                HttpsURLConnection.HTTP_UNAUTHORIZED -> errorMessage.value = R.string.str_authe
                HttpsURLConnection.HTTP_FORBIDDEN, HttpsURLConnection.HTTP_INTERNAL_ERROR, HttpsURLConnection.HTTP_NOT_FOUND -> responseMessage.value =
                    error.message
                else -> responseMessage.value = error.message
            }
        } else if (error is SocketTimeoutException) {
            errorMessage.value = R.string.text_all_has_error_timeout
        } else if (error is IOException) {
            errorMessage.value = R.string.text_all_has_error_network
        } else {
            if (!TextUtils.isEmpty(error.message)) {
                responseMessage.value = error.message
            } else {
                errorMessage.value = R.string.text_all_has_error_please_try
            }
        }
    }

    fun toMultipartBody(name: String, file: File): MultipartBody.Part {
        val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, reqFile)
    }

    fun toMultipartBody1(name: String, file: File): MultipartBody.Part {
        val reqFile = file.asRequestBody("video/*, image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name, file.name, reqFile)
    }

}