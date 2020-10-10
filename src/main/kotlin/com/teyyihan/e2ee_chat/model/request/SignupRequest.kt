package com.teyyihan.e2ee_chat.model.request

import java.util.HashSet
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SignupRequest(
        @NotBlank
        @Size(max = 20)
        val username: String,

        @NotBlank @Size(max = 120)
        val password: String,

        @NotBlank
        val fcmToken: String,

        @NotBlank
        val publicKey: String
)