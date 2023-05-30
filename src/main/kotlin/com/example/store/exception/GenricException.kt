package com.example.store.exception

import org.springframework.http.HttpStatus

class GenricException : RuntimeException {
    override val message: String?
    private var code: HttpStatus? = null

    constructor(message: String?) : super(message) {
        this.message = message
    }

    constructor(message: String?, code: HttpStatus?) : super(message) {
        this.message = message
        this.code = code
    }

    constructor(message: String?, t: Throwable?) : super(message, t) {
        this.message = message
        this.code = null
    }

    fun getCode(): HttpStatus? {
        return code
    }

    fun setCode(code: HttpStatus?) {
        this.code = code
    }

    companion object {
        /**
         *
         */
        private const val serialVersionUID = 1L
    }
}