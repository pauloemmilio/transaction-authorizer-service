package com.transaction.authorizer.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class BalanceRequestDTO(
    @NotBlank
    val accountId: String,
    @field:NotBlank
    val transactionCategory: String,
    @field:Positive
    val amount: BigDecimal
)
