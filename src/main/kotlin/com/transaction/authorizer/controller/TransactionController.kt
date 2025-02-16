package com.transaction.authorizer.controller

import com.transaction.authorizer.dto.request.TransactionRequestDTO
import com.transaction.authorizer.dto.response.TransactionResponseDTO
import com.transaction.authorizer.service.TransactionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Transactions", description = "Endpoints for processing transactions")
@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {

    @Operation(summary = "Process a transaction", description = "Receives data from a transaction and returns the response.")
    @PostMapping("/process")
    fun processTransaction(
        @RequestBody(required = true) transaction: TransactionRequestDTO
    ): ResponseEntity<TransactionResponseDTO> {
        val response = transactionService.process(
            transaction.account, transaction.amount, transaction.mcc, transaction.merchant
        )
        val transactionResponseDTO = TransactionResponseDTO(
            code = response.code,
            message = response.message
        )
        return ResponseEntity.ok(transactionResponseDTO)
    }
}