package com.atipera.recruitment.feature.repositories.web

import com.atipera.recruitment.feature.repositories.application.RepositoryService
import com.atipera.recruitment.feature.repositories.exception.UsernameNotFound
import com.atipera.recruitment.feature.repositories.web.response.BranchResponse
import com.atipera.recruitment.feature.repositories.web.response.RepositoryResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [RepositoryController::class])
class RepositoryControllerTest(
    @Autowired private val mockMvc: MockMvc,
) {
    @MockkBean
    private lateinit var repositoryService: RepositoryService

    @Test
    fun `given wrong accept header should return 406`() {
        //given & when & then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/repositories?username=Eliathen")
                .header("Accept", MediaType.APPLICATION_XML_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isNotAcceptable)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `given correct accept header should return 200`() {
        //given
        every { repositoryService.getRepositories(any()) } returns listOf(getRepositoryResponse())
        //when & then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/repositories?username=Eliathen")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `given not existing username should return 404`() {
        //given
        every { repositoryService.getRepositories(any()) } throws UsernameNotFound("Username not found")
        //when & then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/repositories?username=Eliathen")
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `given existing username should return 200`() {
        //given
        every { repositoryService.getRepositories(any()) } returns listOf(getRepositoryResponse())
        //when & then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/repositories?username=Eliathen")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
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