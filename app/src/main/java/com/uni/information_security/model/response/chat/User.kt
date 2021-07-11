package com.uni.information_security.model.response.chat

import javax.crypto.spec.SecretKeySpec

data class User(
    val id: String? = null,
    val username: String? = null,
    val password: String? = null,
    val avatar: String? = null
)
