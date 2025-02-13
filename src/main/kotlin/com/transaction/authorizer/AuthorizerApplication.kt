package com.transaction.authorizer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthorizerApplication

fun main(args: Array<String>) {
	runApplication<AuthorizerApplication>(*args)
}
