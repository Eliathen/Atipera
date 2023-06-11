package com.atipera.recruitment.feature.repositories.web

import com.atipera.recruitment.feature.repositories.application.RepositoryService
import com.atipera.recruitment.feature.repositories.exception.UsernameNotFound
import com.atipera.recruitment.feature.repositories.web.response.BranchResponse
import com.atipera.recruitment.feature.repositories.web.response.RepositoryResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient


@OptIn(ExperimentalCoroutinesApi::class)
@WebMvcTest(controllers = [RepositoryController::class])
class RepositoryControllerTest {

    @MockkBean
    private lateinit var repositoryService: RepositoryService

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `given wrong accept header should return 406`() {
        //given & when & then
        webTestClient.get()
            .uri("/repositories?username=Eliathen")
            .header("Accept", MediaType.APPLICATION_XML_VALUE)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE)
    }

    @Test
    fun `given correct accept header should return 200`() = runTest {
        //given
        coEvery { repositoryService.getRepositories(any()) } returns listOf(getRepositoryResponse())
        //when & then

        webTestClient.get()
            .uri("/repositories?username=Eliathen")
            .header("Accept", MediaType.APPLICATION_JSON_VALUE)
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun `given not existing username should return 404`() = runTest {
        //given
        coEvery { repositoryService.getRepositories(any()) } throws UsernameNotFound("Username not found")
        //when & then
        webTestClient.get()
            .uri("/repositories?username=Eliathen")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `given existing username should return 200`() = runTest {
        //given
        coEvery { repositoryService.getRepositories(any()) } returns listOf(getRepositoryResponse())
        //when & then
        webTestClient.get()
            .uri("/repositories?username=Eliathen")
            .exchange()
            .expectStatus().isOk
            .expectBody(Array<RepositoryResponse>::class.java)
    }

    private fun getRepositoryResponse() = RepositoryResponse(
        name = "Kipme-internet-bookshop-backend",
        login = "Eliathen",
        branches = listOf(getBranchResponse())
    )

    private fun getBranchResponse() = BranchResponse(
        name = "main",
        lastCommitSha = "fdac41423166ed6b62b5b6aa033671a90e9d9098"
    )
}