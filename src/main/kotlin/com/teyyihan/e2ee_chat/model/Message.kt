package com.teyyihan.e2ee_chat.model

import java.time.LocalDateTime

data class Message(
        val to: String,
        val data: MessageData
)

data class MessageData(
        val fromUsername: String,
        val cipherText: ByteArray,
        val date: String = LocalDateTime.now().toString()
)