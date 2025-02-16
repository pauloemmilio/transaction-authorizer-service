package com.transaction.authorizer.service

import com.transaction.authorizer.entity.TransactionHistory
import com.transaction.authorizer.repository.TransactionHistoryRepository
import org.springframework.stereotype.Service

@Service
class TransactionHistoryService(
    private val transactionHistoryRepository: TransactionHistoryRepository
) {

    fun save(transactionHistory: TransactionHistory) {
        transactionHistoryRepository.save(transactionHistory)
    }

    fun findByAccountId(accountId: String): List<TransactionHistory> {
        return transactionHistoryRepository.findByAccountId(accountId)
    }
}