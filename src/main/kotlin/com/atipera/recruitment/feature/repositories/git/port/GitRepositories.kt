package com.atipera.recruitment.feature.repositories.git.port

import com.atipera.recruitment.feature.repositories.domain.Branch
import com.atipera.recruitment.feature.repositories.domain.Repository

interface GitRepositories {

    fun getRepositoriesForUser(username: String): List<Repository>

    fun getBranchesForUserRepository(username: String, repositoryName: String): List<Branch>
}