package com.transaction.authorizer.repository

import com.transaction.authorizer.entity.Balance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BalanceRepository : JpaRepository<Balance, Long> {
    fun findByAccountIdAndTransactionCategory(accountId: String, name: String): Optional<Balance>
}