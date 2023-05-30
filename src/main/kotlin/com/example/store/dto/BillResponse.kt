package com.example.store.dto

class BillResponse {
    private var netPayableAmount: Int? = null

    fun setNetPayableAmount(amount: Int){
        netPayableAmount = amount
    }

    fun getNetPayableAmount(): Int? {
        return netPayableAmount;
    }
}