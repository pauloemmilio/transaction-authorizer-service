package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Balance
import com.transaction.authorizer.enums.ResponseCodeEnum
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionService(
    private val accountService: AccountService,
    private val transactionCategoryService: TransactionCategoryService,
    private val balanceService: BalanceService
) {

    fun authorizeTransaction(accountId: String, amount: BigDecimal, mcc: String, merchant: String): ResponseCodeEnum {

        val balance: Balance

        try {
            val account = accountService.findById(accountId)
            val transactionCategory = transactionCategoryService.findByCode(mcc)
            balance = balanceService.findByAccountIdAndTransactionCategory(accountId, transactionCategory.name)
        } catch (e: Exception) {
            return ResponseCodeEnum.ERROR
        }

        val transactionResponse = processTransaction(amount, balance)
        return transactionResponse
    }

    private fun processTransaction(amount: BigDecimal, balance: Balance): ResponseCodeEnum {
        if(amount > balance.availableAmount) {
            return ResponseCodeEnum.REJECTED
        }

        val updatedBalance = balance.copy(availableAmount = balance.availableAmount - amount)
        balanceService.update(updatedBalance)

        return ResponseCodeEnum.APPROVED
    }
}