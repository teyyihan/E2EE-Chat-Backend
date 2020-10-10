package com.teyyihan.e2ee_chat.network

data class Result(
        val message_id: String
)

data class MessageResponse(
        val multicast_id: Long,
        val success: Int,
        val failure: Int,
        val canonical_ids: Int,
        val results: Array<Result>
)