package com.atipera.recruitment.feature.repositories.web

import com.atipera.recruitment.feature.repositories.application.RepositoryService
import com.atipera.recruitment.feature.repositories.web.response.RepositoryResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/repositories")
class RepositoryController(private val repositoryService: RepositoryService) {

    @GetMapping
    fun getRepositories(
        @RequestHeader(name = "Accept", defaultValue = "application/json") acceptHeader: String,
        @RequestParam(
            name = "username"
        ) username: String
    ): ResponseEntity<List<RepositoryResponse>> {
        if (acceptHeader == MediaType.APPLICATION_XML_VALUE) throw HttpMediaTypeNotAcceptableException("XML is not acceptable format")
        val response = repositoryService.getRepositories(username)
        return ResponseEntity.ok(response)
    }

}