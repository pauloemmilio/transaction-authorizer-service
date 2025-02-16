package com.transaction.authorizer.dto.request

import java.math.BigDecimal

data class BalanceRequestDTO(
    val transactionCategory: String,
    val amount: BigDecimal
)
