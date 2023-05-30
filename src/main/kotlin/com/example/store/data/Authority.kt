package com.example.store.data

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "authority")
class Authority() {
    @Id
    private var name: String? = null

    fun getName(): String? {
        return name
    }
    fun setName(name: String) {
        this.name = name
    }
}