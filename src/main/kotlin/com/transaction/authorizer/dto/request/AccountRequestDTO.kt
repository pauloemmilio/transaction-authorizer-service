package com.transaction.authorizer.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class AccountRequestDTO(
    @NotBlank
    val accountId: String,
    @NotEmpty
    val balance: List<AccountBalanceRequestDTO>
)
