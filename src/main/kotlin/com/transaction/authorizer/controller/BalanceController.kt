package com.transaction.authorizer.controller

import com.transaction.authorizer.dto.request.BalanceRequestDTO
import com.transaction.authorizer.service.BalanceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Balance", description = "Endpoints for processing balances")
@RestController
@RequestMapping("/api/v1/balances")
class BalanceController(
    private val balanceService: BalanceService
) {

    @Operation(summary = "Adds balance to an account", description = "Receives data from a balance and returns the response.")
    @PostMapping("/add")
    fun addBalance(
        @Validated @RequestBody(required = true) balance: BalanceRequestDTO
    ): ResponseEntity<Void> {
        balanceService.addBalance(
            balance.accountId, balance.amount, balance.transactionCategory
        )
        return ResponseEntity.ok().build()
    }
}