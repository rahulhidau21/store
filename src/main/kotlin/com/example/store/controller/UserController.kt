package com.example.store.controller

import com.example.store.dto.ResponseMessage
import com.example.store.dto.UserRequest
import com.example.store.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users/")
class UserController {
    @Autowired
    private lateinit var userService: IUserService

    @PostMapping("user")
    fun save(@RequestBody userRequest: UserRequest?): Any? {
        userService?.save(userRequest)
        return ResponseEntity<Any?>(ResponseMessage(HttpStatus.OK.value(), "User Saved Successfully"), HttpStatus.OK)
    }
}