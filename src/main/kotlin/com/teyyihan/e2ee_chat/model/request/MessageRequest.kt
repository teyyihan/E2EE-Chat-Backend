package com.teyyihan.e2ee_chat.model.request

data class MessageRequest(
        val toUsername: String,
        val fromUsername: String,
        val cipherText: ByteArray
)