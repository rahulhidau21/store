package com.example.store.service

import com.example.store.data.User
import com.example.store.dto.UserRequest

interface IUserService {
    fun getUser(username: String?): User?
    fun save(userRequest: UserRequest?)
    fun getCurrentUser(): User?
}