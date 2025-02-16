package com.transaction.authorizer.dto.request

import java.math.BigDecimal

data class AccountBalanceRequestDTO(
    val transactionCategory: String,
    val amount: BigDecimal
)
