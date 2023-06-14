package com.atipera.recruitment.feature.repositories.application

import com.atipera.recruitment.feature.repositories.git.port.GitRepositories
import com.atipera.recruitment.feature.repositories.web.response.BranchResponse
import com.atipera.recruitment.feature.repositories.web.response.RepositoryResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service


@Service
class RepositoryService(private val gitRepositories: GitRepositories) {

    suspend fun getRepositories(username: String): List<RepositoryResponse> {
        return coroutineScope() {
            val repositories = gitRepositories.getRepositoriesForUser(username)
            repositories.filter { !it.fork }.map { repository ->
                async {
                    val branches = gitRepositories.getBranchesForUserRepository(username, repository.name)
                        .map { BranchResponse(it.name, it.commit.sha) }
                    RepositoryResponse(repository.name, repository.owner.login, branches)
                }
            }.awaitAll()
        }
    }
}
