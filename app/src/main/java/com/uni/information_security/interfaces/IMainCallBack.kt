package com.uni.information_security.interfaces

import com.uni.information_security.model.response.chat.Group

interface IMainCallBack {
    fun changeFragmentCallBack(isGroupFragment: Boolean, data: Group?)
    fun userUnavailable()
    fun updateUIUser()
}