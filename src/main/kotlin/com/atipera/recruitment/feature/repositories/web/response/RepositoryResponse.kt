package com.atipera.recruitment.feature.repositories.web.response

data class RepositoryResponse(
    val name: String,
    val login: String,
    val branches: List<BranchResponse>
)

data class BranchResponse(
    val name: String,
    val lastCommitSha: String
)