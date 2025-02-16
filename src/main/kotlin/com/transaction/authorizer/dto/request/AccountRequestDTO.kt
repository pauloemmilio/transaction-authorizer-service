package com.transaction.authorizer.dto.request

data class AccountRequestDTO(
    val accountId: String,
    val balance: List<AccountBalanceRequestDTO>
)
