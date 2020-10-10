package com.teyyihan.e2ee_chat.repository

import com.teyyihan.e2ee_chat.model.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface UserRepository: ReactiveCrudRepository<User, String> {

    suspend fun findByUsername(username: String): User?

    suspend fun existsByUsername(username: String): Boolean

    suspend fun save(user: User): User

}