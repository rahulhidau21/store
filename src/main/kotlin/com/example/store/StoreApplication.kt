package com.example.store

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class StoreApplication
fun main(args: Array<String>) {
    runApplication<StoreApplication>(*args)
}