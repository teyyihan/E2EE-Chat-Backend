package com.teyyihan.e2ee_chat.controller

import com.teyyihan.e2ee_chat.model.User
import com.teyyihan.e2ee_chat.model.request.SignupRequest
import com.teyyihan.e2ee_chat.repository.UserRepository
import kotlinx.coroutines.reactor.mono
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.Valid


@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var keycloak: Keycloak

    private val realm: String = "E2EE"

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody signupRequest: SignupRequest): Mono<ResponseEntity<*>> = mono {

        if(userRepository.existsByUsername(signupRequest.username)){    // username already taken
            return@mono ResponseEntity.status(400).body("Username already taken")

        }else{  // username is free
            val credentials = createCredentials(signupRequest.password)
            val userRepresentation = createUserRepresentation(signupRequest.username, credentials)
            val result = keycloak.realm(realm).users().create(userRepresentation)

            if(result.status == 201){
                val user = User(signupRequest.username,signupRequest.publicKey,signupRequest.fcmToken)
                userRepository.save(user)
                return@mono ResponseEntity.ok(user)
            }else{
                return@mono ResponseEntity.status(400).body("Error occurred on keycloak")
            }
        }

    }

    private fun createUserRepresentation(_username: String, _credentials: CredentialRepresentation) =
            UserRepresentation().apply {
                username = _username
                isEnabled = true
                credentials = listOf(_credentials)
                realmRoles = listOf("user")
            }

    private fun createCredentials(password: String) =
            CredentialRepresentation().apply {
            type = CredentialRepresentation.PASSWORD
            value = password
            isTemporary = false
    }
}