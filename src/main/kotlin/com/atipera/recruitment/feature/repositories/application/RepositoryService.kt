package com.atipera.recruitment.feature.repositories.application

import com.atipera.recruitment.feature.repositories.git.port.GitRepositories
import com.atipera.recruitment.feature.repositories.web.response.BranchResponse
import com.atipera.recruitment.feature.repositories.web.response.RepositoryResponse
import org.springframework.stereotype.Service


@Service
class RepositoryService(private val gitRepositories: GitRepositories) {

    fun getRepositories(username: String): List<RepositoryResponse> {
        val repositories = gitRepositories.getRepositoriesForUser(username)

        return repositories.filter { !it.fork }.map { repository ->
            val branches = gitRepositories.getBranchesForUserRepository(username, repository.name)
                .map { BranchResponse(it.name, it.commit.sha) }
            RepositoryResponse(repository.name, repository.owner.login, branches)
        }
    }

}