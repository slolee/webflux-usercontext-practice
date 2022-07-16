package com.example.reactive_demo.config.context

import reactor.core.publisher.Mono
import reactor.util.context.Context

object UserContextHolder {
    private const val CONTEXT_KEY = "USER_CONTEXT"

    fun setContext(
        userContext: UserContext,
        context: Context
    ) = context.put(CONTEXT_KEY, userContext)
    fun getContext(): Mono<UserContext> = Mono.deferContextual { context -> Mono.just(context[CONTEXT_KEY]) }

}

data class UserContext(
    val payAccount: PayAccount
)

data class PayAccount(
    val payAccountId: Long,
    val email: String
)