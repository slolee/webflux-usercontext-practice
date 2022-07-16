package com.example.reactive_demo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@SpringBootApplication
class ReactiveDemoApplication {
    @Bean
    fun objectMapper(): ObjectMapper {
        return Jackson2ObjectMapperBuilder()
            .failOnEmptyBeans(false)
            .failOnUnknownProperties(false)
            .modules(KotlinModule.Builder().build())
            .build()
    }
}

fun main(args: Array<String>) {
    runApplication<ReactiveDemoApplication>(*args)
}
