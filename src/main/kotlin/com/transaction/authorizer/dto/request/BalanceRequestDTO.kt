package com.transaction.authorizer.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class BalanceRequestDTO(
    @NotBlank
    val accountId: String,
    @NotBlank
    val transactionCategory: String,
    @Positive
    val amount: BigDecimal
)
