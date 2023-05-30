package com.example.store.controller

import com.example.store.dto.BillRequest
import com.example.store.dto.ResponseMessage
import com.example.store.service.IBillService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/bill/")
class BillController {
    @Autowired
    private lateinit var billService: IBillService

    @PostMapping("generate")
    fun generate(@RequestBody request: MutableList<BillRequest?>?): Any? {
        return ResponseEntity<Any?>(ResponseMessage(HttpStatus.OK.value(),
                "Bill Generated Successfully", billService?.generate(request)
        ),
                HttpStatus.OK)
    }
}