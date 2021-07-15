package com.uni.information_security.model.response.chat

data class Group(
    val id: String? = null,
    val name: String? = null,
    val avatar: String? = null,
    val userList: MutableMap<String?, UserInGroup?>? = null,
    val messageList: MutableMap<String?, Message?>? = null
)
