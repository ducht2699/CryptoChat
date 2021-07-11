package com.uni.information_security.model.response


data class BaseResponse <T> (
        val body: T,
        val message: String
)
data class BaseArrayResponse <T> (
        val body: List<T>,
        val message: String
)
data class BaseResponseNoBody (
        val message: String
)