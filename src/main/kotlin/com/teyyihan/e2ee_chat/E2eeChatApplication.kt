package com.teyyihan.e2ee_chat

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class E2eeChatApplication{


    private val serverUrl: String = "http://keycloak:8081/auth"
//    private val serverUrl: String = "http://localhost:8081/auth"

    private val realm: String = "master"

    private val clientId: String = "admin-cli"

    private val username: String = "teyyihan"

    private val password: String = "teyyihan"

    @Bean
    fun provideKeycloak(): Keycloak{
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .username(username)
                .password(password)
                .build()
    }
}

fun main(args: Array<String>) {
    runApplication<E2eeChatApplication>(*args)
}


