package com.example.reactive_demo.controller

import com.example.reactive_demo.config.context.RequestContextHolder
import com.example.reactive_demo.config.context.UserContext
import com.example.reactive_demo.config.context.UserContextHolder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@RestController
class TestController(
    val webClient: WebClient
) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(TestController::class.java)
    }

    @GetMapping("/test/{id}")
    fun startTest(@PathVariable id: String): Mono<String>
        = UserContextHolder.getContext().map { userContext ->
                printLog(id, "start", userContext)
                userContext
            }.flatMap { userContext ->
                webClient.get().uri("/delay/2").exchangeToMono {
                    response -> response.bodyToMono(String::class.java).doOnNext { printLog(id, "end", userContext) }
                }
            }

    @GetMapping("/request")
    fun request(
        @RequestParam id: String
    ): Mono<String> {
        return RequestContextHolder.getRequest().map { request -> request.uri.query }
    }

    private fun printLog(id: String, status: String, userContext: UserContext) {
        logger.info("$status -> ThreadInfo(${Thread.currentThread().name}, $id) UserContext(${userContext.payAccount.payAccountId}, ${userContext.payAccount.email})")
    }
}