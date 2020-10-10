package com.teyyihan.e2ee_chat.model.request

import javax.validation.constraints.NotBlank

data class LoginRequest(
        @NotBlank
        val username: String,

        @NotBlank
        val password: String
)