package com.example.store.repository

import com.example.store.data.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User?, Long?> {
    fun findByEmail(email: String?): Optional<User?>?
    fun findAllByEmail(email: String?): MutableList<User?>?
}