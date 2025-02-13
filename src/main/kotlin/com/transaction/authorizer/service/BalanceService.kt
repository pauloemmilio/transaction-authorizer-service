package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Balance
import com.transaction.authorizer.exception.ResourceNotFoundException
import com.transaction.authorizer.repository.BalanceRepository
import org.springframework.stereotype.Service

@Service
class BalanceService(
    private val balanceRepository: BalanceRepository
) {

    fun findByAccountIdAndTransactionCategory(accountId: String, name: String): Balance {
        return balanceRepository.findByAccountIdAndTransactionCategory(accountId, name)
            .orElseThrow { ResourceNotFoundException("Balance for account $accountId and transaction category $name not found") }
    }
}