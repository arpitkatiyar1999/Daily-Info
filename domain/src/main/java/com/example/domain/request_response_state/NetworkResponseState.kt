package com.example.domain.request_response_state

data class NetworkResponseState<out T>(val status: NetworkResponseStatusEnum, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): NetworkResponseState<T> {
            return NetworkResponseState(NetworkResponseStatusEnum.SUCCESS, data, null)
        }


        fun <T> error(msg: String, data: T? = null): NetworkResponseState<T> {
            return NetworkResponseState(NetworkResponseStatusEnum.EXCEPTION, data, msg)
        }
    }
}