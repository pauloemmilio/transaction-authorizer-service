package com.transaction.authorizer.dto.request

import java.math.BigDecimal

data class TransactionRequestDTO(
    //TODO add not blank and not null validations
    val account: String,
    val amount: BigDecimal,
    val mcc: String,
    val merchant: String
)
