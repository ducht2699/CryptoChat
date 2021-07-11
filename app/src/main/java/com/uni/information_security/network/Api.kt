package com.uni.information_security.network

import com.uni.information_security.model.response.BaseResponse
import com.uni.information_security.model.request.LoginRequest
import com.uni.information_security.model.request.SocialRequest
import com.uni.information_security.model.response.User
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    /**
     * Get the list of the pots from the API
     */

    @POST("v1/auth/signin")
    fun loginManual(@Body request: LoginRequest): Observable<BaseResponse<User>>

    @POST("v1/auth/login-facebook")
    fun loginFacebook(@Body request: SocialRequest): Observable<BaseResponse<User>>

    @POST("v1/auth/login-google")
    fun loginGoogle(@Body request: SocialRequest): Observable<BaseResponse<User>>
}