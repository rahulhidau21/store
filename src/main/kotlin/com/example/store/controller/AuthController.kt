package com.example.store.controller

import com.example.store.dto.LoginVM
import com.example.store.dto.ResponseMessage
import com.example.store.security.JwtTokenUtil
import com.example.store.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/")
class AuthController {
    @Autowired
    private lateinit var userService: IUserService

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    private lateinit var authenticationManagerBuilder: AuthenticationManagerBuilder

    @PostMapping("/authenticate")
    fun login(@RequestBody loginVM: LoginVM?): Any {
        val authenticationToken = UsernamePasswordAuthenticationToken(
            loginVM?.getUsername() ?:  "",
            loginVM?.getPassword() ?:""
        )
        val user = userService.getUser(loginVM?.getUsername())
        val authentication = authenticationManagerBuilder.getObject()?.authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtTokenUtil.createToken(authentication, true, user)
        val httpHeaders = HttpHeaders()
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer $jwt")
        return ResponseEntity<Any?>(ResponseMessage(HttpStatus.OK.value(), "Token Generated Successfully", jwt),
                httpHeaders, HttpStatus.OK)
    }

    @PostMapping("/ping")
    fun ping(): String {
        return "pong"
    }
}