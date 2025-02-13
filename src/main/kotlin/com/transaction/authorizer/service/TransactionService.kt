package com.transaction.authorizer.service

import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionService {

    fun authorizeTransaction(account: String, amount: BigDecimal, mcc: String, merchant: String): String {
        //TODO implement authorization logic
        return ""
    }
}