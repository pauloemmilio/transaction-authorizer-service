package com.transaction.authorizer.dto.request

import java.math.BigDecimal

data class TransactionRequestDTO(
    val account: String,
    val amount: BigDecimal,
    val mcc: String,
    val merchant: String
)
