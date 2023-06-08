package com.atipera.recruitment.feature.repositories.application

import com.atipera.recruitment.feature.repositories.domain.Branch
import com.atipera.recruitment.feature.repositories.domain.Commit
import com.atipera.recruitment.feature.repositories.domain.Owner
import com.atipera.recruitment.feature.repositories.domain.Repository
import com.atipera.recruitment.feature.repositories.git.port.GitRepositories
import com.atipera.recruitment.feature.repositories.web.response.RepositoryResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RepositoryServiceTest() {

    @Autowired
    private lateinit var sut: RepositoryService

    @MockkBean
    private lateinit var gitRepositories: GitRepositories

    @Test
    fun `given list with one repository which is fork should return empty list`() {
        val repository = getRepositoryWithFork()
        val username = "Eliathen"
        every { gitRepositories.getRepositoriesForUser(username) } returns listOf(repository)
        val result: List<RepositoryResponse> = sut.getRepositories(username)

        assert(result.isEmpty())
    }

    @Test
    fun `given username should return list of repositories with branches`() {
        //given
        val repository = getRepository()
        val username = "Eliathen"
        every { gitRepositories.getRepositoriesForUser(username) } returns listOf(repository)

        val branch = getBranch()
        every { gitRepositories.getBranchesForUserRepository(username, repository.name) } returns listOf(branch)

        //when
        val result: List<RepositoryResponse> = sut.getRepositories(username)

        //then
        assert(result.first().name == repository.name)
        assert(result.first().login == repository.owner.login)
        assert(result.first().branches.first().name == branch.name)
        assert(result.first().branches.first().lastCommitSha == branch.commit.sha)

    }

    private fun getRepositoryWithFork(): Repository {
        return Repository(fork = true, id = 0, name = "Repository", owner = Owner("Eliathen"))
    }

    private fun getRepository(): Repository {
        return Repository(fork = false, id = 123, name = "Airport", owner = Owner("Eliathen"))

    }

    private fun getBranch(): Branch {
        return Branch("main", Commit("#123"))
    }
}