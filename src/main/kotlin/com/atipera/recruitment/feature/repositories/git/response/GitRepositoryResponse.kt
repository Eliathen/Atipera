package com.atipera.recruitment.feature.repositories.git.response

import com.atipera.recruitment.feature.repositories.domain.Owner
import com.atipera.recruitment.feature.repositories.domain.Repository
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class GitRepositoryResponse(
    @JsonProperty("fork")
    val fork: Boolean,
    @JsonProperty("id")
    val id: Int,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("owner")
    val owner: GitOwnerResponse
)

fun GitRepositoryResponse.toRepository(): Repository {
    return Repository(fork = fork, id = id, name = name, owner = owner.toOwner())
}

data class GitOwnerResponse(
    @JsonProperty("login")
    val login: String
)

fun GitOwnerResponse.toOwner(): Owner {
    return Owner(login)
}