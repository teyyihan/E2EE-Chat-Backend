package com.teyyihan.e2ee_chat.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
        @Id
        val username: String,
        val publicKey: String,
        val fcmToken: String
)