package com.transaction.authorizer.repository

import com.transaction.authorizer.entity.Balance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BalanceRepository : JpaRepository<Balance, Long> {
    fun findByAccount_AccountIdAndTransactionCategory(accountId: String, name: String): Balance?
}