package com.teyyihan.e2ee_chat.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.teyyihan.e2ee_chat.model.Message
import com.teyyihan.e2ee_chat.model.MessageData
import com.teyyihan.e2ee_chat.model.User
import com.teyyihan.e2ee_chat.model.request.MessageRequest
import com.teyyihan.e2ee_chat.model.request.UserUpdate
import com.teyyihan.e2ee_chat.network.FcmAPI
import com.teyyihan.e2ee_chat.repository.UserRepository
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.annotation.security.RolesAllowed
import javax.validation.Valid


@RestController
@RequestMapping("/resource")
class ResourceController {

    @Autowired
    private lateinit var fcmAPI: FcmAPI

    @Autowired
    private lateinit var userRepository: UserRepository

    @RolesAllowed("user")
    @PostMapping("/send")
    fun sendMessage(@Valid @RequestBody messageRequest: MessageRequest): Mono<ResponseEntity<*>> = mono {
        val userToSend = userRepository.findByUsername(messageRequest.toUsername)
                ?: return@mono ResponseEntity.status(400).body("Wrong username")

        val message = Message(
                to = userToSend.fcmToken,
                data = MessageData(
                        messageRequest.fromUsername,
                        messageRequest.cipherText
                )
        )
        try {
            val response = fcmAPI.sendMessage(message)
            if (response.success == 1 && response.failure == 0) {
                return@mono ResponseEntity.ok(response)
            }
            println("/resource/send error when sending message")
            return@mono ResponseEntity.status(400).body("Error occurred while sending message")
        } catch (e: Exception) {
            println("/resource/send exception ${e.localizedMessage}")
            return@mono ResponseEntity.status(400).body(e.localizedMessage)
        }
    }

    @RolesAllowed("user")
    @GetMapping("/user")
    fun searchUser(
            @RequestParam("username") username: String
    ): Mono<ResponseEntity<*>> = mono {
        val user = userRepository.findByUsername(username)
                ?: return@mono ResponseEntity.status(400).body("User not found")
        return@mono ResponseEntity.ok(user)
    }

    @RolesAllowed("user")
    @PostMapping("/user/update")
    fun updateUser(
            @RequestHeader("Authorization") authHeader: String,
            @Valid @RequestBody userUpdate: UserUpdate
    ): Mono<ResponseEntity<*>> = mono {
        try {

            val usernameFromJWT = JWT.decode(authHeader.replace("Bearer ", ""))
                    .claims["preferred_username"]?.`as`(String::class.java)
                    ?: throw IllegalStateException("Wrong credentials")

            if (userRepository.existsByUsername(usernameFromJWT)) {
                val userToDelete = userRepository.findByUsername(usernameFromJWT)
                userRepository.delete(userToDelete!!)
                val userToSave = User(usernameFromJWT, userUpdate.publicKey
                        ?: userToDelete.publicKey, userUpdate.fcmToken)
                userRepository.save(userToSave)
                return@mono ResponseEntity.ok(userToSave)
            } else {
                throw IllegalStateException("There is no user having that username")
            }

        } catch (e: Exception) {
            return@mono ResponseEntity.badRequest().body(e.localizedMessage)
        }

    }


    @Bean
    fun provideRetrofit(): FcmAPI {
        return Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(FcmAPI::class.java)
    }

}