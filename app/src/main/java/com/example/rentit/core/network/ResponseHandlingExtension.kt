package com.example.rentit.core.network

import com.example.rentit.core.error.BadRequestException
import com.example.rentit.core.error.ConflictException
import com.example.rentit.core.error.ForbiddenException
import com.example.rentit.core.error.NotFoundException
import com.example.rentit.core.error.ServerErrorException
import com.example.rentit.core.error.UnauthorizedException
import com.example.rentit.core.error.UnknownException
import com.example.rentit.core.error.UnprocessableEntityException
import retrofit2.Response

fun <T> Response<T>.getOrThrow(): Result<T> {
    return runCatching {
        if(isSuccessful) {
            body() ?: throw ServerErrorException("Response body is null")
        } else {
            val errorMessage = errorBody()?.string() ?: "Client Error"
            throw when(code()) {
                400 -> BadRequestException(errorMessage)
                401 -> UnauthorizedException(errorMessage)
                403 -> ForbiddenException(errorMessage)
                404 -> NotFoundException(errorMessage)
                409 -> ConflictException(errorMessage)
                422 -> UnprocessableEntityException(errorMessage)
                in 500..599 -> ServerErrorException(errorMessage)
                else -> UnknownException(errorMessage)
            }
        }
    }
}