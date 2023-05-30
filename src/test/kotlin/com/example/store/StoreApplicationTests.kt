package com.example.store

import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [StoreApplication::class])
@AutoConfigureMockMvc
internal class StoreApplicationTests {
    @Test
    fun contextLoads() {
    }
}