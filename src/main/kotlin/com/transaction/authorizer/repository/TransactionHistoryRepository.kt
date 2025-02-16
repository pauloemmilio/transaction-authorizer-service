package com.transaction.authorizer.repository

import com.transaction.authorizer.entity.TransactionHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionHistoryRepository: JpaRepository<TransactionHistory, Long> {
    fun findByAccountId(accountId: String): List<TransactionHistory>
}