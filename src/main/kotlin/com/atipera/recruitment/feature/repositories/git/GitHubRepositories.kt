package com.atipera.recruitment.feature.repositories.git

import com.atipera.recruitment.core.config.GithubProperties
import com.atipera.recruitment.feature.repositories.domain.Branch
import com.atipera.recruitment.feature.repositories.domain.Repository
import com.atipera.recruitment.feature.repositories.exception.RepositoryException
import com.atipera.recruitment.feature.repositories.exception.UsernameNotFound
import com.atipera.recruitment.feature.repositories.git.port.GitRepositories
import com.atipera.recruitment.feature.repositories.git.response.GitBranchResponse
import com.atipera.recruitment.feature.repositories.git.response.GitRepositoryResponse
import com.atipera.recruitment.feature.repositories.git.response.toBranch
import com.atipera.recruitment.feature.repositories.git.response.toRepository
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException.NotFound
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate

@Component
class GitHubRepositories(
    private val restTemplate: RestTemplate,
    private val githubProperties: GithubProperties
) : GitRepositories {
    override fun getRepositoriesForUser(username: String): List<Repository> {
        try {
            val response = restTemplate.getForEntity(
                "${githubProperties.baseUrl}${githubProperties.repositories}",
                Array<GitRepositoryResponse>::class.java,
                username
            )
            return response.body?.map { repository -> repository.toRepository() } ?: emptyList()
        } catch (exception: NotFound) {
            throw UsernameNotFound("Not found user with name: $username")
        } catch (exception: RestClientResponseException) {
            throw RepositoryException("Unexpected situation occurred: " + exception.message)
        }
    }

    override fun getBranchesForUserRepository(username: String, repositoryName: String): List<Branch> {
        try {
            val response = restTemplate.getForEntity(
                "${githubProperties.baseUrl}${githubProperties.branches}",
                Array<GitBranchResponse>::class.java,
                username,
                repositoryName
            )
            return response.body?.map { branch -> branch.toBranch() } ?: emptyList()

        } catch (exception: RestClientResponseException) {
            throw RepositoryException("Unexpected situation occurred: " + exception.message)
        }

    }
}

