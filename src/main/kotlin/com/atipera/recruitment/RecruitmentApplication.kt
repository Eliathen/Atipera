package com.atipera.recruitment

import com.atipera.recruitment.core.config.GithubProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(GithubProperties::class)
class RecruitmentApplication

fun main(args: Array<String>) {
    runApplication<RecruitmentApplication>(*args)
}
