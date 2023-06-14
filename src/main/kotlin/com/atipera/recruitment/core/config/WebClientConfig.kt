package com.atipera.recruitment.core.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Value(value = "\${github.base-url}")
    private lateinit var githubBaseUrl: String

    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .baseUrl(githubBaseUrl)
            .build()
    }
}
