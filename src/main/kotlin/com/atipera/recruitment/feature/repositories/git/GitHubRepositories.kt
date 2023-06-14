package com.atipera.recruitment.feature.repositories.git

import com.atipera.recruitment.core.config.GithubProperties
import com.atipera.recruitment.feature.repositories.domain.Branch
import com.atipera.recruitment.feature.repositories.domain.Repository
import com.atipera.recruitment.feature.repositories.exception.UsernameNotFound
import com.atipera.recruitment.feature.repositories.git.port.GitRepositories
import com.atipera.recruitment.feature.repositories.git.response.GitBranchResponse
import com.atipera.recruitment.feature.repositories.git.response.GitRepositoryResponse
import com.atipera.recruitment.feature.repositories.git.response.toBranch
import com.atipera.recruitment.feature.repositories.git.response.toRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class GitHubRepositories(
    private val webClient: WebClient,
    private val githubProperties: GithubProperties
) : GitRepositories {
    override suspend fun getRepositoriesForUser(username: String): List<Repository> {
        return webClient.get().uri { uriBuilder -> uriBuilder.path(githubProperties.repositories).build(username) }
            .retrieve()
            .onStatus(
                { code -> code.value() == HttpStatus.NOT_FOUND.value() },
                { throw UsernameNotFound("Not found user with name: $username") }
            )
            .awaitBody<List<GitRepositoryResponse>>().map { it.toRepository() }

    }

    override suspend fun getBranchesForUserRepository(username: String, repositoryName: String): List<Branch> {
        return webClient.get()
            .uri { uriBuilder -> uriBuilder.path(githubProperties.branches).build(username, repositoryName) }
            .retrieve()
            .awaitBody<List<GitBranchResponse>>()
            .map { it.toBranch() }
    }
}
