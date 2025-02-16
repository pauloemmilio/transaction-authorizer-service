package com.transaction.authorizer.dto.response

import java.math.BigDecimal

data class TransactionHistoryDTO(
    val amount: BigDecimal,
    val transactionCategory: String,
    val merchant: String,
    val responseCode: String,
    val responseMessage: String
)
