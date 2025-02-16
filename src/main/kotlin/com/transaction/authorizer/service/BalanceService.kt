package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Account
import com.transaction.authorizer.entity.Balance
import com.transaction.authorizer.repository.BalanceRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class BalanceService(
    private val balanceRepository: BalanceRepository,
    private val transactionCategoryService: TransactionCategoryService
) {

    fun update(balance: Balance): Balance {
        return balanceRepository.save(balance)
    }

    fun findByAccountIdAndTransactionCategory(accountId: String, name: String): Balance? {
        return balanceRepository.findByAccount_AccountIdAndTransactionCategory(accountId, name)
    }

    fun createBalance(account: Account, transactionCategory: String, amount: BigDecimal) {
        val balance = Balance(
            account = account,
            transactionCategory = transactionCategory,
            availableAmount = amount
        )
        balanceRepository.save(balance)
    }

    fun addBalance(accountId: String, amount: BigDecimal, transactionCategoryName: String) {
        transactionCategoryService.validatesTransactionCategory(transactionCategoryName)
        val balance = findByAccountIdAndTransactionCategory(accountId, transactionCategoryName)

        if (balance != null) {
            balance.availableAmount = balance.availableAmount.add(amount)
            update(balance)
        } else {
            val account = Account(accountId = accountId)
            createBalance(account, transactionCategoryName, amount)
        }
    }
}