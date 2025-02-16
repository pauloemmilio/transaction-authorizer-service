package com.transaction.authorizer.dto.response

data class TransactionHistoryResponseDTO(
    val accountId: String,
    val transactions: List<TransactionHistoryDTO>
)
