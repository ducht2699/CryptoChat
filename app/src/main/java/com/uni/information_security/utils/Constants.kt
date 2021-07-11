package com.uni.information_security.utils

import com.uni.information_security.injection.ApplicationComponent
import com.uni.information_security.model.response.chat.User


const val PREF_USERNAME: String = "PREF_USERNAME"
const val PREF_PASS: String = "PREF_PASS"
const val PREF_KEY: String = "PREF_KEY"
const val PREF_AUTO_LOGIN: String = "PREF_AUTO_LOGIN"

const val FORMAT_DATE: String = "HH:mm dd/MM/yyyy"

var myapp: ApplicationComponent? = null

const val EMPTY_STRING = ""

const val USER_PATH = "Users"

var USER_DATA: User? = null