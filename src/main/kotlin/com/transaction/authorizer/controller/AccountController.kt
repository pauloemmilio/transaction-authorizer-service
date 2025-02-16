package com.transaction.authorizer.controller

import com.transaction.authorizer.dto.request.AccountRequestDTO
import com.transaction.authorizer.service.AccountService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Accounts", description = "Endpoints for processing accounts")
@RestController
@RequestMapping("/api/v1/accounts")
class AccountController(
    private val accountService: AccountService
) {

    @Operation(summary = "Creates an account", description = "Receives data from an account and returns the response.")
    @PostMapping
    fun createAccount(
        @RequestBody(required = true) account: AccountRequestDTO
    ): ResponseEntity<Void> {
        accountService.createAccount(account.accountId, account.balance)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }
}