package com.atipera.recruitment.feature.repositories.domain

data class Repository(
    val fork: Boolean,
    val id: Int,
    val name: String,
    val owner: Owner
)

