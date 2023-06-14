package com.atipera.recruitment.feature.repositories.git.port

import com.atipera.recruitment.feature.repositories.domain.Branch
import com.atipera.recruitment.feature.repositories.domain.Repository

interface GitRepositories {

    suspend fun getRepositoriesForUser(username: String): List<Repository>

    suspend fun getBranchesForUserRepository(username: String, repositoryName: String): List<Branch>
}