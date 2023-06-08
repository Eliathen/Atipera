package com.atipera.recruitment.feature.repositories.exception

class UsernameNotFound(override val message: String?) : RuntimeException(message) {
}