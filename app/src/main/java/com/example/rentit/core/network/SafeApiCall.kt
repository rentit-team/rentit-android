package com.example.rentit.core.network

import com.example.rentit.core.exceptions.BadRequestException
import com.example.rentit.core.exceptions.ConflictException
import com.example.rentit.core.exceptions.ForbiddenException
import com.example.rentit.core.exceptions.NotFoundException
import com.example.rentit.core.exceptions.ServerErrorException
import com.example.rentit.core.exceptions.UnauthorizedException
import com.example.rentit.core.exceptions.UnknownException
import com.example.rentit.core.exceptions.UnprocessableEntityException
import retrofit2.Response

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
    return runCatching {
        val response = apiCall()

        if(response.isSuccessful) {
            response.body() ?: throw ServerErrorException("Response body is null")
        } else {
            val errorMessage = response.errorBody()?.string() ?: "Client Error"
            throw when(response.code()) {
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