package com.example.store.dto

class BillRequest {
    private var productName: String? = null
    private var price: Int? = null
    private var qty: Int? = null
    private var type: String? = null

    fun getPrice(): Int? {
        return price
    }

    fun setPrice(price: Int) {
        this.price  = price
    }

    fun getQty(): Int? {
        return qty;
    }

    fun setQty(qty: Int) {
        this.qty  = qty
    }

    fun getType(): String? {
        return type
    }

    fun setType(type: String) {
        this.type  = type
    }
}