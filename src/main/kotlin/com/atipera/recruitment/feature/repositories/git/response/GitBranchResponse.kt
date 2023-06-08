package com.atipera.recruitment.feature.repositories.git.response


import com.atipera.recruitment.feature.repositories.domain.Branch
import com.atipera.recruitment.feature.repositories.domain.Commit
import com.fasterxml.jackson.annotation.JsonProperty

data class GitBranchResponse(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("commit")
    val commit: GitCommitResponse,
)

fun GitBranchResponse.toBranch(): Branch {
    return Branch(name, commit.toCommit())
}

data class GitCommitResponse(
    @JsonProperty("sha")
    val sha: String,
)

fun GitCommitResponse.toCommit(): Commit {
    return Commit(sha)
}