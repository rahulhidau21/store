package com.example.store.service.impl

import com.example.store.data.User
import com.example.store.dto.UserRequest
import com.example.store.exception.GenricException
import com.example.store.repository.UserRepository
import com.example.store.service.IUserService
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
open class UserService : IUserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Value("\${jwt.secret}")
    private val secret: String? = null
    override fun getUser(username: String?): User? {
        return userRepository.findByEmail(username)?.orElseThrow { BadCredentialsException("Username not found") }
    }

    override fun save(request: UserRequest?) {
        val user = User()
        if (request != null) {
            request.getName()?.let { user.setName(it) }
            request.getEmail()?.let { user.setEmail(it) }
            request.getPassword()?.let { user.setPassword(it) }
            request.getAuthorities()?.let { user.setAuthorities(it) }
            user.setCreatedDate(LocalDateTime.now())
        }
        user.setCreatedDate( LocalDateTime.now())
        val list = userRepository.findAllByEmail(request?.getEmail())
        if (list != null) {
            if (!list.isEmpty()) {
                throw GenricException("Email already exits", HttpStatus.BAD_REQUEST)
            }
        }
        userRepository.save(user)
    }

    override fun getCurrentUser(): User? {
        val authentication = SecurityContextHolder.getContext().authentication
        val token = authentication.credentials.toString()
        val claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
        val userId = java.lang.Long.valueOf(claims["userId"] as String?)
        return userRepository.findById(userId).orElseThrow { GenricException("User not Found", HttpStatus.NOT_FOUND) }
    }
}