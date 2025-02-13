package com.transaction.authorizer.controller

import com.transaction.authorizer.dto.request.TransactionRequestDTO
import com.transaction.authorizer.dto.response.TransactionResponseDTO
import com.transaction.authorizer.service.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/transactions")
class TransactionController(
    private val transactionService: TransactionService
) {

    @PostMapping("/authorize")
    fun authorizeTransaction(
        @RequestBody(required = true) transaction: TransactionRequestDTO
    ): ResponseEntity<TransactionResponseDTO> {
        val response = transactionService.authorizeTransaction(
            transaction.account, transaction.amount, transaction.mcc, transaction.merchant
        )
        val transactionResponseDTO = TransactionResponseDTO(response)
        return ResponseEntity.ok(transactionResponseDTO)
    }
}