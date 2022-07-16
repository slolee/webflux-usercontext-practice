package com.example.reactive_demo.interceptor

import com.example.reactive_demo.config.context.RequestContextHolder
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
class RequestContextInterceptor: WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        return chain.filter(exchange)
            .contextWrite { ctx -> ctx.put(RequestContextHolder.CONTEXT_KEY, request) }
    }
}
