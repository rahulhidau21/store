package com.example.store.exception

class BadRequestException : RuntimeException {
    override val message: String?

    constructor(message: String?) : super(message) {
        this.message = message
    }

    constructor(message: String?, t: Throwable?) : super(message, t) {
        this.message = message
    }

    companion object {
        /**
         *
         */
        private const val serialVersionUID = 1L
    }
}