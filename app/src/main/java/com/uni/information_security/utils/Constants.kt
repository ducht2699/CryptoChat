package com.uni.information_security.utils

import com.uni.information_security.injection.ApplicationComponent
import com.uni.information_security.model.response.chat.Group
import com.uni.information_security.model.response.chat.User


const val PREF_USERNAME: String = "PREF_USERNAME"
const val PREF_PASS: String = "PREF_PASS"
const val PREF_KEY: String = "PREF_KEY"
const val PREF_AUTO_LOGIN: String = "PREF_AUTO_LOGIN"

const val FORMAT_DATE: String = "HH:mm dd/MM/yyyy"

var myapp: ApplicationComponent? = null

const val EMPTY_STRING = ""

const val USER_PATH = "Users"
const val GROUP_PATH = "Groups"
const val MESSAGE_PATH = "messageList"
const val USER_IN_GROUP_PATH = "userList"

var USER_DATA: User? = null
var GROUP_DATA: Group? = null
const val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 553

const val ALLOWED_CHARACTERS =
    "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"

const val EXTRA_GROUP_ID  = "EXTRA_GROUP_ID"
