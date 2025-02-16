package com.transaction.authorizer.controller

import com.transaction.authorizer.dto.request.TransactionRequestDTO
import com.transaction.authorizer.dto.response.TransactionHistoryDTO
import com.transaction.authorizer.dto.response.TransactionHistoryResponseDTO
import com.transaction.authorizer.dto.response.TransactionResponseDTO
import com.transaction.authorizer.service.TransactionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @Operation(summary = "Search a transaction history", description = "Receives an account id and returns the response.")
    @GetMapping
    fun getHistory(@RequestParam(name = "accountId", required = true) accountId: String): ResponseEntity<TransactionHistoryResponseDTO> {
        val history = transactionService.getHistory(accountId)

        val response = TransactionHistoryResponseDTO(
            accountId = accountId,
            transactions = history.map {
                TransactionHistoryDTO(
                    amount = it.amount,
                    transactionCategory = it.transactionCategory,
                    merchant = it.merchant,
                    responseCode = it.responseCode,
                    responseMessage = it.responseMessage
                )
            }
        )
        return ResponseEntity.ok(response)
    }
}