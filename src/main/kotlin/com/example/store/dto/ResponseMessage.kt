package com.example.store.dto

class ResponseMessage {
    private var status: Int?
    private var message: String?
    private var data: Any?

    constructor(status: Int?, message: String?, data: Any?) {
        this.status = status
        this.message = message
        this.data = data
    }

    constructor(status: Int?, message: String?) {
        this.status = status
        this.message = message
        data = null
    }

    constructor(data: Any?, status: Int?) {
        this.status = status
        message = null
        this.data = data
    }

    fun getStatus(): Int? {
        return status
    }

    fun setStatus(status: Int?) {
        this.status = status
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getData(): Any? {
        return data
    }

    fun setData(data: Any?) {
        this.data = data
    }
}