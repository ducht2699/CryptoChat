package com.uni.information_security.model.response.chat

data class Group(
    val id: String?,
    val name: String?,
    val avatar: String?,
    val userList: List<UserInGroup>?,
    val messageList: List<Message?>?

)
