package com.atipera.recruitment.core.exception

import com.atipera.recruitment.feature.repositories.exception.UsernameNotFound
import com.fasterxml.jackson.annotation.JsonAutoDetect.*
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.*

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [(HttpMediaTypeNotAcceptableException::class)])
    fun notAcceptable(exception: HttpMediaTypeNotAcceptableException): ResponseEntity<ErrorInfo> {
        val errorInfo = ErrorInfo(HttpStatus.NOT_ACCEPTABLE.value(), exception.message)
        return ResponseEntity
            .status(HttpStatus.NOT_ACCEPTABLE)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorInfo)
    }

    @ExceptionHandler(value = [(UsernameNotFound::class)])
    fun notAcceptable(exception: UsernameNotFound): ResponseEntity<ErrorInfo> {
        val errorInfo = ErrorInfo(HttpStatus.NOT_FOUND.value(), exception.message)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorInfo)
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun unexpected(exception: Exception): ResponseEntity<ErrorInfo> {
        val errorInfo = ErrorInfo(HttpStatus.NOT_FOUND.value(), exception.message)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(errorInfo)
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorInfo(
    @get:JsonProperty("status") val status: Int?,
    @get:JsonProperty("Message") val message: String?
)