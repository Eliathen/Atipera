package com.atipera.recruitment.feature.repositories.git

import com.atipera.recruitment.feature.repositories.domain.Branch
import com.atipera.recruitment.feature.repositories.domain.Commit
import com.atipera.recruitment.feature.repositories.domain.Owner
import com.atipera.recruitment.feature.repositories.domain.Repository
import com.atipera.recruitment.feature.repositories.exception.UsernameNotFound
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.web.client.RestTemplate


@SpringBootTest
class GitHubRepositoriesTest {

    @Autowired
    private lateinit var restTemplate: RestTemplate

    private lateinit var mockServer: MockRestServiceServer

    @Autowired
    private lateinit var gitHubRepositories: GitHubRepositories

    private val objectMapper: ObjectMapper = ObjectMapper()

    @BeforeEach
    fun init() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @Test
    fun `given username should return list of repositories`() {
        //given
        val repository = getRepository()
        val username = "Eliathen"
        mockServer.expect(MockRestRequestMatchers.requestTo("https://api.github.com/users/$username/repos"))
            .andRespond(
                MockRestResponseCreators.withSuccess(
                    objectMapper.writeValueAsString(
                        listOf(repository)
                    ),
                    MediaType.APPLICATION_JSON
                )
            )
        //when
        val result = gitHubRepositories.getRepositoriesForUser(username)
        //then
        Assertions.assertEquals(result.size, 1)
    }

    @Test
    fun `given not existing username should throw username not found exception`() {
        //given
        val username = "Eliathen"
        mockServer.expect(MockRestRequestMatchers.requestTo("https://api.github.com/users/$username/repos"))
            .andRespond(
                MockRestResponseCreators.withResourceNotFound()
            )
        //when & then
        Assertions.assertThrows(UsernameNotFound::class.java) { gitHubRepositories.getRepositoriesForUser(username) }
    }

    @Test
    fun `given username and repository should return list of branches`() {
        val repository = getRepository()
        val branches = getBranches()
        val username = "Eliathen"
        mockServer.expect(MockRestRequestMatchers.requestTo("https://api.github.com/repos/${username}/${repository.name}/branches"))
            .andRespond(
                MockRestResponseCreators.withSuccess(
                    objectMapper.writeValueAsString(
                        branches
                    ),
                    MediaType.APPLICATION_JSON
                )
            )
        //when
        val result = gitHubRepositories.getBranchesForUserRepository(username, repository.name)
        //then
        Assertions.assertEquals(result.first().name, branches.first().name)
        Assertions.assertEquals(result.size, branches.size)

    }

    private fun getRepository(): Repository {
        return Repository(fork = false, id = 123, name = "Airport", owner = Owner("Eliathen"))
    }

    private fun getBranches(): List<Branch> {
        return listOf(Branch("main", Commit("#123")), Branch("development", Commit("#223")))
    }
}