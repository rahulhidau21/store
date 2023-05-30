package com.example.store.service

import com.example.store.dto.BillRequest
import com.example.store.dto.BillResponse

interface IBillService {
    open fun generate(request: MutableList<BillRequest?>?): BillResponse?
}