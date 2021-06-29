package com.uni.information_security.model.request


import com.google.gson.annotations.SerializedName

data class SocialRequest(
    @SerializedName("token")
    val token: String
)