package com.example.reactive_demo.interceptor

import com.example.reactive_demo.config.context.PayAccount
import com.example.reactive_demo.config.context.UserContext
import com.example.reactive_demo.config.context.UserContextHolder
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
class UserContextInterceptor(
    val objectMapper: ObjectMapper
): WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val xPayAccount = exchange.request.headers.getFirst("x-pay-account")

        val payAccount = try {
            objectMapper.readValue(xPayAccount, PayAccount::class.java)
        } catch (e: Exception) {
            throw RuntimeException()
        }

        return chain.filter(exchange)
            .contextWrite { context -> UserContextHolder.setContext(UserContext(payAccount), context) }

    }
}

