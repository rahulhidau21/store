package com.example.store.dto

import com.example.store.data.Authority

class UserRequest {
    private var name: String? = null
    private var password: String? = null
    private var email: String? = null
    private var authorities: Authority? = null

    fun getName(): String? {
        return name
    }
    fun setName(name: String) {
        this.name = name
    }

    fun getPassword(): String? {
        return password
    }
    fun setPassword(name: String) {
        this.password = password
    }

    fun getEmail(): String? {
        return email
    }
    fun setEmail(name: String) {
        this.email = email
    }

    fun getAuthorities(): Authority? {
        return authorities
    }

    fun setAuthorities(authorities: Authority) {
        this.authorities = authorities
    }
}