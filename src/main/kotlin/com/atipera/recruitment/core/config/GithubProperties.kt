package com.atipera.recruitment.core.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("github")
data class GithubProperties(
    val baseUrl: String,
    val repositories: String,
    val branches: String
)