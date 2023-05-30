package com.example.store.data

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private var id: Long? = null
    private var name: String? = null
    private var password: String? = null
    private var email: String? = null
    private var isDeleted = false
    private var createdDate: LocalDateTime? = null

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private var authorities: Authority? = null



    constructor(
        id: Long?,
        name: String?,
        password: String?,
        email: String?,
        isDeleted: Boolean,
        createdDate: LocalDateTime?,
        authorities: Authority?
    ) {
        this.id = id
        this.name = name
        this.password = password
        this.email = email
        this.isDeleted = isDeleted
        this.createdDate = createdDate
        this.authorities = authorities
    }

    constructor()

    fun getId(): Long? {
        return id
    }
    fun setId(id: Long) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }
    fun setName(name: String) {
        this.name = name
    }

    fun getPassword(): String? {
        return password
    }
    fun setPassword(password: String) {
        this.password = password
    }

    fun getEmail(): String? {
        return email
    }
    fun setEmail(email: String) {
        this.email = email
    }

    fun getAuthorities(): Authority? {
        return authorities
    }

    fun setAuthorities(authorities: Authority) {
        this.authorities = authorities
    }

    fun getCreatedDate(): LocalDateTime? {
        return createdDate
    }

    fun setCreatedDate(createdDate : LocalDateTime) {
        this.createdDate = createdDate
    }

}