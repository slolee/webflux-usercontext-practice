package com.example.reactive_demo.config.context

import org.springframework.http.server.reactive.ServerHttpRequest
import reactor.core.publisher.Mono

object RequestContextHolder {
    const val CONTEXT_KEY = "REQUEST_CONTEXT"
    fun getRequest(): Mono<ServerHttpRequest> = Mono.deferContextual { ctx -> Mono.just(ctx[CONTEXT_KEY]) }
}

