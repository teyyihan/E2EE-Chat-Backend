package com.teyyihan.e2ee_chat.model.request

data class UserUpdate(
        val publicKey: String?,
        val fcmToken: String
)