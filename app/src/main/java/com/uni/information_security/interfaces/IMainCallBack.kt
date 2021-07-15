package com.uni.information_security.interfaces

interface IMainCallBack {
    fun changeFragmentCallBack(isGroupFragment: Boolean, data: String?)
    fun userUnavailable()
    fun updateUIUser()
}