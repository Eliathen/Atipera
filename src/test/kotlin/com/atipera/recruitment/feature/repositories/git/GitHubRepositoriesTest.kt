package com.atipera.recruitment.feature.repositories.git

import com.atipera.recruitment.core.config.GithubProperties
import com.atipera.recruitment.feature.repositories.domain.Branch
import com.atipera.recruitment.feature.repositories.domain.Commit
import com.atipera.recruitment.feature.repositories.domain.Owner
import com.atipera.recruitment.feature.repositories.domain.Repository
import com.atipera.recruitment.feature.repositories.exception.UsernameNotFound
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient


@OptIn(ExperimentalCoroutinesApi::class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GitHubRepositoriesTest {

    private var server: MockWebServer = MockWebServer()

    private lateinit var gitHubRepositories: GitHubRepositories

    private lateinit var webClient: WebClient

    private val objectMapper: ObjectMapper = ObjectMapper()

    @BeforeAll
    fun init() {
        server.start()
        val baseUrl = "http://localhost:${server.port}"
        webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build()
        gitHubRepositories = GitHubRepositories(
            webClient,
            GithubProperties(baseUrl, "users/{username}/repos", "users/{username}/{repositoryName}/branches")
        )
    }

    @AfterAll
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `given username should return list of repositories`() = runTest {
        //given
        val repository = getRepository()
        val username = "Eliathen"
        server.enqueue(
            MockResponse().setBody(
                objectMapper.writeValueAsString(
                    listOf(repository)
                )
            ).setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )
        //when
        val result = gitHubRepositories.getRepositoriesForUser(username)
        //then
        Assertions.assertEquals(1, result.size)
    }

    @Test
    fun `given not existing username should throw username not found exception`() = runTest {
        //given
        val username = "Eliathen"
        server.enqueue(
            MockResponse().setResponseCode(HttpStatus.NOT_FOUND.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        )
        //when & then
        assertThrows<UsernameNotFound> {
            gitHubRepositories.getRepositoriesForUser(username)
        }
    }

    @Test
    fun `given username and repository should return list of branches`() = runTest {
        val repository = getRepository()
        val branches = getBranches()
        val username = "Eliathen"
        server.enqueue(
            MockResponse().setBody(
                objectMapper.writeValueAsString(
                    branches
                )
            ).setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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